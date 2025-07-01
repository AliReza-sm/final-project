package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.*;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final ReviewRepository reviewRepository;
    private final SpecialistRepository specialistRepository;
    private final ModelMapper modelMapper;

    public CustomerResponseDto register(CustomerRegistrationDto dto){
        if (customerRepository.findByEmail(dto.email()).isPresent()){
            throw new AlreadyExistException("email already exist");
        }
        Customer customer = modelMapper.map(dto, Customer.class);
        Wallet wallet = new Wallet();
        wallet.setUser(customer);
        customer.setWallet(wallet);
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponseDto(savedCustomer.getId(),
                savedCustomer.getFirstname(), savedCustomer.getLastname(),
                savedCustomer.getEmail(), savedCustomer.getRegistrationDate());
    }

    public OrderResponseDto createOrder(Long customerId, OrderRequestDto dto){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
        Service service = serviceRepository.findById(dto.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
        if (dto.proposedPrice() < service.getBasePrice()){
            throw new NotAllowedException("proposed price can not be less than base price");
        }
        Order order = modelMapper.map(dto, Order.class);
        order.setCustomer(customer);
        order.setService(service);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDto(savedOrder.getId(), savedOrder.getProposedPrice(),
                savedOrder.getDescription(), savedOrder.getAddress()
        ,savedOrder.getProposedStartDate(), savedOrder.getOrderStatus(),
                savedOrder.getService(), savedOrder.getCustomer(),
                savedOrder.getOffers());
    }

    public void selectOfferForOrder(Long customerId, Long orderId, Long offerId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
        if (!order.getCustomer().getId().equals(customerId)){
            throw new NotAllowedException("customer not authorized");
        }
        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_OFFER_SELECTION){
            throw new NotAllowedException("order not waiting for offer selection");
        }
        Offer selectedOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("offer not found"));
        if (!selectedOffer.getOrder().getId().equals(orderId)){
            throw new NotAllowedException("offer does not belong to this order");
        }
        order.setSelectedOffer(selectedOffer);
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        selectedOffer.setOfferStatus(OfferStatus.ACCEPTED);
        order.getOffers().stream()
                .filter(offer -> !offer.getId().equals(offerId))
                .forEach(offer -> offer.setOfferStatus(OfferStatus.REJECTED));
        orderRepository.save(order);
    }

    public void payForOrder(Long customerId, Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
        if (!order.getCustomer().getId().equals(customerId)){
            throw new NotAllowedException("customer not authorized");
        }
        if (order.getOrderStatus() != OrderStatus.DONE){
            throw new NotAllowedException("order must be in DONE state to be paid");
        }
        Offer acceptedOffer = order.getSelectedOffer();
        Double price = acceptedOffer.getProposedPrice();
        Wallet customerWallet = order.getCustomer().getWallet();
        if (customerWallet.getBalance().compareTo(price) < 0){
            throw new IllegalStateException("insufficient funds");
        }
        Wallet specialistWallet = acceptedOffer.getSpecialist().getWallet();
        customerWallet.setBalance(customerWallet.getBalance() - price);
        specialistWallet.setBalance(specialistWallet.getBalance() + price);
        order.setOrderStatus(OrderStatus.PAID);
    }

    public ReviewResponseDto leaveReview(Long customerId, ReviewRequestDto dto){
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
        if (!order.getCustomer().getId().equals(customerId)){
            throw new NotAllowedException("customer not authorized");
        }
        if (order.getOrderStatus() != OrderStatus.PAID){
            throw new NotAllowedException("order must be in PAID state");
        }
        Review review = modelMapper.map(dto, Review.class);
        review.setCustomer(order.getCustomer());
        review.setSpecialist(order.getSelectedOffer().getSpecialist());
        review.setOrder(order);
        Review savedReview = reviewRepository.save(review);
        updateSpecialistScore(order.getSelectedOffer().getSpecialist(), savedReview.getRating());
        return modelMapper.map(savedReview, ReviewResponseDto.class);
    }

    private void updateSpecialistScore(Specialist specialist, Integer rating) {
        specialist.setSumScore(specialist.getSumScore() + rating);
        specialist.setNumberOfReviews(specialist.getNumberOfReviews() + 1);
        specialist.setAverageScore(specialist.getSumScore()/specialist.getNumberOfReviews());
        specialistRepository.save(specialist);
    }

    public Double showWalletBalance(Long customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
        return customer.getWallet().getBalance();
    }

    public void addFundsToWallet(Long customerId, Double amount){
        if (amount <= 0){
            throw new IllegalArgumentException("amount must be greater than 0");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
        Wallet wallet = customer.getWallet();
        wallet.setBalance(wallet.getBalance() + amount);
    }
}
