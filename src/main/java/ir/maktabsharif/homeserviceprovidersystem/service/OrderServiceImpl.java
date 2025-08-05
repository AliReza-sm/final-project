package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.OfferHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.ReviewHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.ServiceHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.SpecialistHelperService;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ir.maktabsharif.homeserviceprovidersystem.repository.OrderSpecification.*;

@org.springframework.stereotype.Service
@Transactional
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final SpecialistHelperService specialistService;
    private final ServiceHelperService serviceHelperService;
    private final WalletService walletService;
    private final ReviewHelperService reviewHelperService;
    private final OfferHelperService offerHelperService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerService customerService,
                            SpecialistHelperService specialistService,
                            ServiceHelperService serviceHelperService,
                            WalletService walletService,
                            ReviewHelperService reviewHelperService, OfferHelperService offerHelperService) {
        super(orderRepository);
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.specialistService = specialistService;
        this.serviceHelperService = serviceHelperService;
        this.walletService = walletService;
        this.reviewHelperService = reviewHelperService;
        this.offerHelperService = offerHelperService;
    }


    @Override
    public boolean existByServiceId(Long id) {
        return orderRepository.existsByServiceId(id);
    }

    @Override
    public OrderDto.OrderResponseDto createOrder(OrderDto.OrderRequestDto requestDto, Long customerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Service service = serviceHelperService.findById(requestDto.getServiceId())
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
        Specialist specialist = specialistService.findById(specialistId)
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
        boolean isOrderSpecialist = specialistService.findByEmail(email)
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

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You cannot pay this order.");
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
        walletService.withdrawFromWallet(order.getCustomer().getEmail(), price);
        walletService.depositToWallet(acceptedOffer.getSpecialist().getEmail(), price * 0.7);
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);
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
            specialistService.save(specialist);
        }
    }

    @Override
    public Page<OrderDto.CustomerOrderHistoryDto> getOrderHistoryForCustomer(Long customerId, OrderStatus status, Pageable pageable) {
        customerService.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Page<Order> ordersPage;
        if (status != null) {
            ordersPage = orderRepository.findAllByCustomerIdAndOrderStatus(customerId, status, pageable);
        } else {
            ordersPage = orderRepository.findAllByCustomerId(customerId, pageable);
        }
        return ordersPage.map(OrderDto::mapToCustomerOrderHistoryDto);
    }

    @Override
    public Page<OrderDto.ManagerOrderHistorySummaryDto> getOrderHistoryForManager(OrderFilterDto filter, Pageable pageable) {
        Specification<Order> spec = getSpec(filter);
        Page<Order> ordersPage = orderRepository.findAll(spec, pageable);
        return ordersPage.map(OrderDto::mapToManagerOrderHistorySummaryDto);
    }

    private Specification<Order> getSpec(OrderFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            Specification<Order> spec = Specification.not(null);

            if (filter.getOrderStatus() != null) {
                spec = spec.and(hasStatus(filter.getOrderStatus()));
            }
            if (filter.getServiceId() != null) {
                spec = spec.and(forService(filter.getServiceId()));
            }
            if (filter.getCustomerId() != null) {
                spec = spec.and(forCustomer(filter.getCustomerId()));
            }
            if (filter.getSpecialistId() != null) {
                spec = spec.and(forSpecialist(filter.getSpecialistId()));
            }
            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                if (filter.getStartDate().isAfter(filter.getEndDate())){
                    throw new NotAllowedException("Start date cannot be after end date.");
                }
                spec = spec.and(inDateRange(
                        filter.getStartDate().atStartOfDay(),
                        filter.getEndDate().atStartOfDay()
                ));
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }

    @Override
    public OrderDto.ManagerOrderDetailDto getManagerOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        Optional<Review> reviewByOrderId = reviewHelperService.findByOrderId(orderId);
        return reviewByOrderId.map(review -> OrderDto.mapToManagerOrderDetailDto(order, reviewByOrderId.get())).orElseGet(() -> OrderDto.mapToManagerOrderDetailDto(order, null));
    }

    @Override
    public Page<OrderDto.SpecialistOrderHistoryDto> getOrderHistoryForSpecialist(Long specialistId, Pageable pageable) {
        specialistService.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with id: " + specialistId));

        Page<Offer> specialistOffers = offerHelperService.findBySpecialistId(specialistId, pageable);

        return specialistOffers.map(OrderDto::mapToSpecialistOrderHistoryDto);
    }


    @Override
    public OrderDto.SpecialistOrderDetailDto getOrderDetailForSpecialist(Long orderId, Long specialistId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getSelectedOffer().getSpecialist().getId().equals(specialistId)) {
            throw new NotAllowedException("Specialist does not belong to this order");
        }

        if (order.getSelectedOffer().getSpecialist().getAccountStatus() != AccountStatus.APPROVED ){
            throw new NotAllowedException("Specialist is not approved");
        }

        Offer specialistOffer = order.getOffers().stream()
                .filter(offer -> offer.getSpecialist().getId().equals(specialistId))
                .findFirst()
                .orElseThrow(() -> new NotAllowedException("You have not placed an offer on this order and are not authorized to view its details."));

        Integer rating = reviewHelperService.findByOrderId(orderId)
                .map(Review::getRating)
                .orElse(null);

        return OrderDto.mapToSpecialistOrderDetailDto(order, specialistOffer, rating);
    }

}
