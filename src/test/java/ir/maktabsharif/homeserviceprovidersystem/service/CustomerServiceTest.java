//package ir.maktabsharif.homeserviceprovidersystem.service;
//
//import ir.maktabsharif.homeserviceprovidersystem.dto.*;
//import ir.maktabsharif.homeserviceprovidersystem.entity.*;
//import ir.maktabsharif.homeserviceprovidersystem.repository.*;
//import ir.maktabsharif.homeserviceprovidersystem.util.MyValidator;
//import jakarta.validation.Validator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CustomerServiceTest {
//
//    @Mock
//    private CustomerRepository customerRepository;
//    @Mock
//    private ServiceRepository serviceRepository;
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private OfferRepository offerRepository;
//    @Mock
//    private ReviewRepository reviewRepository;
//    @Mock
//    private SpecialistRepository specialistRepository;
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private CustomerService customerService;
//    private Customer testCustomer;
//    private Order testOrder;
//    private Offer testOffer;
//    private Review testReview;
//    private Service testService;
//
//    @BeforeEach
//    void setUp() {
//        testCustomer = new Customer();
//        testCustomer.setId(1L);
//        testCustomer.setWallet(new Wallet());
//        testCustomer.getWallet().setBalance(100D);
//
//        Specialist specialist = new Specialist();
//        specialist.setWallet(new Wallet());
//        specialist.setSumScore(20D);
//        specialist.setAverageScore(0D);
//        specialist.setNumberOfReviews(4);
//
//        testOrder = new Order();
//        testOrder.setId(100L);
//        testOrder.setCustomer(testCustomer);
//        testOrder.setOffers(Mockito.mock(List.class));
//
//
//        testOffer = new Offer();
//        testOffer.setId(1000L);
//        testOffer.setOrder(testOrder);
//        testOffer.setProposedPrice(80.0);
//        testOffer.setSpecialist(specialist);
//
//        testReview = new Review();
//        testReview.setId(1L);
//        testReview.setCustomer(testCustomer);
//        testReview.setOrder(testOrder);
//        testReview.setSpecialist(specialist);
//        testReview.setRating(5);
//
//        testService = new Service();
//    }
//
//    @Test
//    void register() {
//        CustomerDto.CustomerRequestDto dto = new CustomerDto.CustomerRequestDto();
//        dto.setFirstName("a");
//        dto.setLastName("a");
//        dto.setEmail("a@mail.com");
//        dto.setPassword("AaBbCc11");
//        when(customerRepository.save(any())).thenReturn(testCustomer);
//        customerService.register(dto);
//        verify(customerRepository, times(1)).save(any(Customer.class));
//    }
//
//    @Test
//    void updateSpecialist(){
//        CustomerDto.CustomerUpdateDto dto = new CustomerDto.CustomerUpdateDto();
//        dto.setEmail("a@mail.com");
//        dto.setPassword("AaBbCc11");
//        dto.setFirstName("a");
//        dto.setLastName("a");
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
//        when(customerRepository.update(any())).thenReturn(testCustomer);
//        customerService.update(1L, dto);
//        verify(customerRepository, times(1)).update(testCustomer);
//        assertEquals("a", testCustomer.getFirstname());
//    }
//
//    @Test
//    void createOrder() {
//        OrderDto.OrderRequestDto dto = new OrderDto.OrderRequestDto();
//        dto.setServiceId(1L);
//        dto.setProposedPrice(60D);
//        dto.setDescription("aa");
//        dto.setAddress("aa");
//        dto.setProposedStartDate(LocalDateTime.now().plusDays(1));
//        Service service = new Service();
//        service.setBasePrice(50.0);
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
//        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));
//        when(orderRepository.save(any())).thenReturn(testOrder);
//        customerService.createOrder(1L, dto);
//        verify(orderRepository, times(1)).save(any(Order.class));
//    }
//
//    @Test
//    void selectOfferForOrder() {
//        testOrder.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
//        when(orderRepository.findById(100L)).thenReturn(Optional.of(testOrder));
//        when(offerRepository.findById(1000L)).thenReturn(Optional.of(testOffer));
//        customerService.selectOfferForOrder(1L, 100L, 1000L);
//        assertEquals(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE, testOrder.getOrderStatus());
//        assertEquals(testOffer, testOrder.getSelectedOffer());
//    }
//
//    @Test
//    void payForOrder() {
//        testOrder.setOrderStatus(OrderStatus.DONE);
//        testOrder.setSelectedOffer(testOffer);
//        when(orderRepository.findById(100L)).thenReturn(Optional.of(testOrder));
//        customerService.payForOrder(1L, 100L);
//        assertEquals(20D, testCustomer.getWallet().getBalance());
//        assertEquals(80D, testOffer.getSpecialist().getWallet().getBalance());
//        assertEquals(OrderStatus.PAID, testOrder.getOrderStatus());
//    }
//
//    @Test
//    void leaveReview() {
//        ReviewDto.ReviewRequestDto dto = new ReviewDto.ReviewRequestDto();
//        dto.setComment("");
//        dto.setOrderId(10L);
//        dto.setRating(5);
//        testOrder.setOrderStatus(OrderStatus.PAID);
//        testOrder.setSelectedOffer(testOffer);
//        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));
//        when(reviewRepository.save(any())).thenReturn(testReview);
//        customerService.leaveReview(1L, dto);
//        verify(reviewRepository, times(1)).save(any(Review.class));
//        verify(specialistRepository, times(1)).update(any(Specialist.class));
//    }
//
//    @Test
//    void showWalletBalance() {
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
//        Double balance = customerService.showWalletBalance(1L);
//        assertEquals(100D, balance);
//    }
//
//    @Test
//    void addFundsToWallet() {
//        Double amountToAdd = 50D;
//        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
//        customerService.addFundsToWallet(1L, amountToAdd);
//        assertEquals(150D, testCustomer.getWallet().getBalance());
//    }
//
//    @Test
//    void showAllServices(){
//        when(serviceRepository.findAll()).thenReturn(Collections.singletonList(testService));
//        List<ServiceDto.ServiceResponseDto> services = customerService.showAllServices();
//        assertFalse(services.isEmpty());
//        assertEquals(1, services.size());
//    }
//}