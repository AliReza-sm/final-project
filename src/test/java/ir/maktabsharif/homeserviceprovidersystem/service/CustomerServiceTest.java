package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerRegistrationDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderRequestDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewRequestDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerService customerService;
    private Customer testCustomer;
    private Order testOrder;
    private Offer testOffer;
    private Review testReview;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setWallet(new Wallet());
        testCustomer.getWallet().setBalance(100D);

        Specialist specialist = new Specialist();
        specialist.setWallet(new Wallet());
        specialist.setSumScore(20D);
        specialist.setAverageScore(0D);
        specialist.setNumberOfReviews(4);

        testOrder = new Order();
        testOrder.setId(100L);
        testOrder.setCustomer(testCustomer);
        testOrder.setOffers(Mockito.mock(List.class));


        testOffer = new Offer();
        testOffer.setId(1000L);
        testOffer.setOrder(testOrder);
        testOffer.setProposedPrice(80.0);
        testOffer.setSpecialist(specialist);

        testReview = new Review();
        testReview.setId(1L);
        testReview.setCustomer(testCustomer);
        testReview.setOrder(testOrder);
        testReview.setSpecialist(specialist);
        testReview.setRating(5);
    }

    @Test
    void register() {
        CustomerRegistrationDto dto = new CustomerRegistrationDto("a", "a", "a@mail.com", "AaBbCc11");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(any(), any())).thenReturn(new Customer());
        when(customerRepository.save(any())).thenReturn(testCustomer);
        customerService.register(dto);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createOrder() {
        OrderRequestDto dto = new OrderRequestDto(1L, 60D, "aa", "aa", LocalDateTime.now().plusDays(1));
        Service service = new Service();
        service.setBasePrice(50.0);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(serviceRepository.findById(any())).thenReturn(Optional.of(service));
        when(orderRepository.save(any())).thenReturn(testOrder);
        customerService.createOrder(1L, dto);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void selectOfferForOrder() {
        testOrder.setOrderStatus(OrderStatus.WAITING_FOR_OFFER_SELECTION);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(testOrder));
        when(offerRepository.findById(1000L)).thenReturn(Optional.of(testOffer));
        customerService.selectOfferForOrder(1L, 100L, 1000L);
        assertEquals(OrderStatus.IN_PROGRESS, testOrder.getOrderStatus());
        assertEquals(testOffer, testOrder.getSelectedOffer());
    }

    @Test
    void payForOrder() {
        testOrder.setOrderStatus(OrderStatus.DONE);
        testOrder.setSelectedOffer(testOffer);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(testOrder));
        customerService.payForOrder(1L, 100L);
        assertEquals(20D, testCustomer.getWallet().getBalance());
        assertEquals(80D, testOffer.getSpecialist().getWallet().getBalance());
        assertEquals(OrderStatus.PAID, testOrder.getOrderStatus());
    }

    @Test
    void leaveReview() {
        ReviewRequestDto dto = new ReviewRequestDto(5,"", 10L);
        testOrder.setOrderStatus(OrderStatus.PAID);
        testOrder.setSelectedOffer(testOffer);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(testOrder));
        when(modelMapper.map(any(), any())).thenReturn(new Review());
        when(reviewRepository.save(any())).thenReturn(testReview);
        customerService.leaveReview(1L, dto);
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(specialistRepository, times(1)).update(any(Specialist.class));
    }

    @Test
    void showWalletBalance() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        Double balance = customerService.showWalletBalance(1L);
        assertEquals(100D, balance);
    }

    @Test
    void addFundsToWallet() {
        Double amountToAdd = 50D;
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        customerService.addFundsToWallet(1L, amountToAdd);
        assertEquals(150D, testCustomer.getWallet().getBalance());
    }
}