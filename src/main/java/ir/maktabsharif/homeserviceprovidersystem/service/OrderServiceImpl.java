package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ServiceRepository serviceRepository;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;


    @Override
    public OrderDto.OrderResponseDto createOrder(OrderDto.OrderRequestDto requestDto, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Service service = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + requestDto.getServiceId()));

        if (requestDto.getProposedPrice() < service.getBasePrice()) {
            throw new NotAllowedException("Proposed price cannot be less than the service base price.");
        }

        Order order = OrderDto.mapToEntity(requestDto);
        order.setCustomer(customer);
        order.setService(service);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);

        Order savedOrder = orderRepository.save(order);
        return OrderDto.mapToDto(savedOrder);
    }

    @Override
    public List<OrderDto.OrderResponseDto> findAvailableOrdersForSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with id: " + specialistId));

        List<OrderStatus> relevantStatuses = List.of(
                OrderStatus.WAITING_FOR_SPECIALIST_OFFERS,
                OrderStatus.WAITING_FOR_OFFER_SELECTION
        );

        return orderRepository.findByOrderStatusInAndServiceIn(relevantStatuses, specialist.getSpecialistServices())
                .stream()
                .map(OrderDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto.OrderResponseDto findOrderById(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        boolean isCustomer = order.getCustomer().getEmail().equals(email);
        boolean isOrderSpecialist = specialistRepository.findByEmail(email)
                .map(s -> s.getSpecialistServices().contains(order.getService()))
                .orElse(false);

        if (!isCustomer && !isOrderSpecialist) {
            throw new NotAllowedException("You are not allowed to view this order.");
        }

        return OrderDto.mapToDto(order);
    }

    @Override
    public void markWorkAsStarted(Long orderId, Long customerId) {
        Order order = getOrderAndVerifyCustomer(orderId, customerId);

        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE) {
            throw new NotAllowedException("Order is not in the WAITING_FOR_SPECIALIST_TO_ARRIVE state.");
        }
        if (LocalDateTime.now().isBefore(order.getSelectedOffer().getProposedStartTime())) {
            throw new NotAllowedException("Cannot mark work as started before the specialist's proposed start time.");
        }

        order.setOrderStatus(OrderStatus.WORK_STARTED);
        orderRepository.save(order);
    }

    private Order getOrderAndVerifyCustomer(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You are not the owner of this order.");
        }
        return order;
    }

    @Override
    public void markWorkAsDone(Long orderId, Long customerId) {
        Order order = getOrderAndVerifyCustomer(orderId, customerId);

        if (order.getOrderStatus() != OrderStatus.WORK_STARTED) {
            throw new NotAllowedException("Order is not in the WORK_STARTED state.");
        }
        order.setWorkCompletedDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.DONE);
        orderRepository.save(order);
    }

    @Override
    public void payForOrder(Long orderId, Long customerId) {
        Order order = getOrderAndVerifyCustomer(orderId, customerId);

        if (order.getOrderStatus() != OrderStatus.DONE) {
            throw new NotAllowedException("Order must be in DONE state to be paid.");
        }

        Offer acceptedOffer = order.getSelectedOffer();
        if (acceptedOffer == null) {
            throw new NotAllowedException("No offer has been selected for this order.");
        }

        modifyMinesPointsForSpecialist(order, acceptedOffer);

        Double price = acceptedOffer.getProposedPrice();

        Wallet customerWallet = order.getCustomer().getWallet();
        if (customerWallet.getBalance() < price) {
            throw new NotAllowedException("Insufficient funds in your wallet.");
        }
        Wallet specialistWallet = acceptedOffer.getSpecialist().getWallet();
        customerWallet.setBalance(customerWallet.getBalance() - price);
        specialistWallet.setBalance(specialistWallet.getBalance() + (price * 0.7));
        modifyTransaction(orderId, customerWallet, price, TransactionType.PAYMENT_SENT);
        modifyTransaction(orderId, specialistWallet, price * 0.7, TransactionType.PAYMENT_RECEIVED);
        walletRepository.save(customerWallet);
        walletRepository.save(specialistWallet);
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }

    private void modifyTransaction(Long orderId, Wallet wallet, Double price, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(price * 0.7);
        transaction.setType(transactionType);
        transaction.setDescription("Payment for Order: " + orderId);
        transactionRepository.save(transaction);
    }

    private void modifyMinesPointsForSpecialist(Order order, Offer acceptedOffer) {
        long delayInHours = Duration.between(acceptedOffer.getProposedStartTime(), order.getWorkCompletedDate()).toHours() - acceptedOffer.getTimeToEndTheJobInHours();
        Specialist specialist = acceptedOffer.getSpecialist();
        if (delayInHours > 0) {
            specialist.setSumScore(specialist.getSumScore() - delayInHours);
            if (specialist.getSumScore() < 0) {
                specialist.setSumScore(0.0);
                specialist.setSpecialistStatus(SpecialistStatus.INACTIVE);
            }
            if (specialist.getNumberOfReviews() > 0) {
                specialist.setAverageScore(specialist.getSumScore() / specialist.getNumberOfReviews());
            } else {
                specialist.setAverageScore(0.0);
            }
            specialistRepository.save(specialist);
        }
    }

}
