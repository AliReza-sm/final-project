//package ir.maktabsharif.homeserviceprovidersystem.service;
//
//import ir.maktabsharif.homeserviceprovidersystem.dto.*;
//import ir.maktabsharif.homeserviceprovidersystem.entity.*;
//import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
//import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
//import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
//import ir.maktabsharif.homeserviceprovidersystem.repository.*;
//import ir.maktabsharif.homeserviceprovidersystem.util.MyValidator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@org.springframework.stereotype.Service
//@RequiredArgsConstructor
//@Transactional
//public class CustomerService {
//
//    private final CustomerRepository customerRepository;
//    private final ServiceRepository serviceRepository;
//    private final OrderRepository orderRepository;
//    private final OfferRepository offerRepository;
//    private final ReviewRepository reviewRepository;
//    private final SpecialistRepository specialistRepository;
//
//    public CustomerDto.CustomerResponseDto register(CustomerDto.CustomerRequestDto dto){
//        if (customerRepository.findByEmail(dto.getEmail()).isPresent()){
//            throw new AlreadyExistException("email already exist");
//        }
//        Customer customer = CustomerDto.mapToEntity(dto);
//        Wallet wallet = new Wallet();
//        wallet.setUser(customer);
//        customer.setWallet(wallet);
//        Customer savedCustomer = customerRepository.save(customer);
//        return CustomerDto.mapToDto(savedCustomer);
//    }
//
//    public CustomerDto.CustomerResponseDto update(Long customerId, CustomerDto.CustomerUpdateDto dto){
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
//        if (dto.getEmail() != null){
//            if (customerRepository.findByEmail(dto.getEmail()).isPresent()){
//                throw new AlreadyExistException("email already exist");
//            }
//            customer.setEmail(dto.getEmail());
//        }
//        if (dto.getFirstName() != null){
//            customer.setFirstname(dto.getFirstName());
//        }
//        if (dto.getLastName() != null){
//            customer.setLastname(dto.getLastName());
//        }
//        if (dto.getPassword() != null){
//            customer.setPassword(dto.getPassword());
//        }
//        Customer updateCustomer = customerRepository.update(customer);
//        return CustomerDto.mapToDto(updateCustomer);
//    }
//
//    public List<ServiceDto.ServiceResponseDto> showAllServices(){
//        return serviceRepository.findAll().stream().map(ServiceDto::mapToDto).collect(Collectors.toList());
//    }
//
//    public OrderDto.OrderResponseDto createOrder(Long customerId, OrderDto.OrderRequestDto dto){
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
//        Service service = serviceRepository.findById(dto.getServiceId())
//                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
//        if (dto.getProposedPrice() < service.getBasePrice()){
//            throw new NotAllowedException("proposed price can not be less than base price");
//        }
//        Order order = OrderDto.mapToEntity(dto);
//        order.setCustomer(customer);
//        order.setService(service);
//        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
//        Order savedOrder = orderRepository.save(order);
//        return OrderDto.mapToDto(savedOrder);
//    }
//
//    public void selectOfferForOrder(Long customerId, Long orderId, Long offerId){
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
//        if (!order.getCustomer().getId().equals(customerId)){
//            throw new NotAllowedException("customer not authorized");
//        }
//        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_OFFER_SELECTION){
//            throw new NotAllowedException("order not waiting for offer selection");
//        }
//        Offer selectedOffer = offerRepository.findById(offerId)
//                .orElseThrow(() -> new ResourceNotFoundException("offer not found"));
//        if (!selectedOffer.getOrder().getId().equals(orderId)){
//            throw new NotAllowedException("offer does not belong to this order");
//        }
//        order.setSelectedOffer(selectedOffer);
//        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
//        selectedOffer.setOfferStatus(OfferStatus.ACCEPTED);
//        order.getOffers().stream()
//                .filter(offer -> !offer.getId().equals(offerId))
//                .forEach(offer -> offer.setOfferStatus(OfferStatus.REJECTED));
//        orderRepository.save(order);
//    }
//
//    public void payForOrder(Long customerId, Long orderId){
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
//        if (!order.getCustomer().getId().equals(customerId)){
//            throw new NotAllowedException("customer not authorized");
//        }
//        if (order.getOrderStatus() != OrderStatus.DONE){
//            throw new NotAllowedException("order must be in DONE state to be paid");
//        }
//        Offer acceptedOffer = order.getSelectedOffer();
//        Double price = acceptedOffer.getProposedPrice();
//        Wallet customerWallet = order.getCustomer().getWallet();
//        if (customerWallet.getBalance().compareTo(price) < 0){
//            throw new IllegalStateException("insufficient funds");
//        }
//        Wallet specialistWallet = acceptedOffer.getSpecialist().getWallet();
//        customerWallet.setBalance(customerWallet.getBalance() - price);
//        specialistWallet.setBalance(specialistWallet.getBalance() + price);
//        order.setOrderStatus(OrderStatus.PAID);
//    }
//
//    public ReviewDto.ReviewResponseDto leaveReview(Long customerId, ReviewDto.ReviewRequestDto dto){
//        Order order = orderRepository.findById(dto.getOrderId())
//                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
//        if (!order.getCustomer().getId().equals(customerId)){
//            throw new NotAllowedException("customer not authorized");
//        }
//        if (order.getOrderStatus() != OrderStatus.PAID){
//            throw new NotAllowedException("order must be in PAID state");
//        }
//        Review review = ReviewDto.mapToEntity(dto);
//        review.setCustomer(order.getCustomer());
//        review.setSpecialist(order.getSelectedOffer().getSpecialist());
//        review.setOrder(order);
//        Review savedReview = reviewRepository.save(review);
//        updateSpecialistScore(order.getSelectedOffer().getSpecialist(), savedReview.getRating());
//        return ReviewDto.mapToDto(savedReview);
//    }
//
//    private void updateSpecialistScore(Specialist specialist, Integer rating) {
//        specialist.setSumScore(specialist.getSumScore() + rating);
//        specialist.setNumberOfReviews(specialist.getNumberOfReviews() + 1);
//        specialist.setAverageScore(specialist.getSumScore()/specialist.getNumberOfReviews());
//        specialistRepository.update(specialist);
//    }
//
//    public Double showWalletBalance(Long customerId){
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
//        return customer.getWallet().getBalance();
//    }
//
//    public void addFundsToWallet(Long customerId, Double amount){
//        if (amount <= 0){
//            throw new IllegalArgumentException("amount must be greater than 0");
//        }
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("customer not found"));
//        Wallet wallet = customer.getWallet();
//        wallet.setBalance(wallet.getBalance() + amount);
//    }
//
//    public List<OfferDto.OfferResponseDto> viewOrderOffers(Long customerId, Long orderId, String sortBy) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
//        if (!order.getCustomer().getId().equals(customerId)) {
//            throw new NotAllowedException("Customer is not authorized to view these offers.");
//        }
//        Comparator<Offer> comparator = null;
//        if (sortBy.equals("price")){
//            comparator = Comparator.comparing(Offer::getProposedPrice);
//        }
//        if (sortBy.equals("rating")){
//            comparator = Comparator.comparing(offer -> offer.getSpecialist().getAverageScore());
//        }
//        assert comparator != null;
//        return order.getOffers().stream()
//                .sorted(comparator)
//                .map(OfferDto::mapToDto)
//                .collect(Collectors.toList());
//    }
//
//    public void announceWorkStart(Long customerId, Long orderId){
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
//        if (!order.getCustomer().getId().equals(customerId)) {
//            throw new NotAllowedException("Customer is not authorized to announce work start");
//        }
//        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE){
//            throw new NotAllowedException("order is not in WAITING_FOR_SPECIALIST_TO_ARRIVE");
//        }
//        if (LocalDateTime.now().isAfter(order.getSelectedOffer().getProposedStartTime())){
//            throw new NotAllowedException("Cannot announce start of work before the specialists proposed start time");
//        }
//        order.setOrderStatus(OrderStatus.WORK_STARTED);
//    }
//
//    public void announceWorkEnd(Long customerId, Long orderId){
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new ResourceNotFoundException("order not found"));
//        if (!order.getCustomer().getId().equals(customerId)) {
//            throw new NotAllowedException("Customer is not authorized to announce work end");
//        }
//        if (order.getOrderStatus() != OrderStatus.WORK_STARTED){
//            throw new NotAllowedException("Work has not been started for this order");
//        }
//        order.setOrderStatus(OrderStatus.DONE);
//    }
//}
