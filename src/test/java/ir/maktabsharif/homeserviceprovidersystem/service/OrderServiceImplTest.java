package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.OfferHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.ReviewHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.ServiceHelperService;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.SpecialistHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private ServiceHelperService serviceService;
    @Mock
    private SpecialistHelperService specialistService;
    @Mock
    private ReviewHelperService reviewService;
    @Mock
    private OfferHelperService offerHelperService;
    @Mock
    private WalletService walletService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Specialist specialist;
    private Service service;
    private Order order;
    private OrderDto.OrderRequestDto orderRequestDto;
    private Customer testCustomer;
    private Order testOrder;
    private Offer testOffer;
    private Review review;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");

        service = new Service();
        service.setId(1L);
        service.setName("Cleaning");
        service.setBasePrice(100.0);

        specialist = new Specialist();
        specialist.setId(2L);
        specialist.setEmail("specialist@gmail.com");
        specialist.setSpecialistServices(Set.of(service));
        specialist.setAccountStatus(AccountStatus.APPROVED);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setService(service);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
        order.setSelectedOffer(new Offer());

        review = new Review();
        review.setId(1L);
        review.setOrder(order);
        review.setCustomer(customer);
        review.setSpecialist(specialist);
        review.setRating(3);

        orderRequestDto = new OrderDto.OrderRequestDto();
        orderRequestDto.setServiceId(1L);
        orderRequestDto.setProposedPrice(120.0);
        orderRequestDto.setAddress("eram");
        orderRequestDto.setProposedStartDate(LocalDateTime.now().plusDays(1));

        testCustomer = new Customer();
        testCustomer.setId(2L);
        testCustomer.setWallet(new Wallet());
        testCustomer.setEmail("customer1@gmail.com");
        testCustomer.getWallet().setBalance(100D);

        Specialist testSpecialist = new Specialist();
        testSpecialist.setWallet(new Wallet());
        testSpecialist.setEmail("specialist1@gmail.com");
        testSpecialist.setSumScore(20D);
        testSpecialist.setAverageScore(0D);
        testSpecialist.setNumberOfReviews(4);

        testOrder = new Order();
        testOrder.setId(100L);
        testOrder.setCustomer(testCustomer);
        testOrder.setOffers(Mockito.mock(List.class));
        testOrder.setWorkCompletedDate(LocalDateTime.now().minusHours(3));


        testOffer = new Offer();
        testOffer.setId(1000L);
        testOffer.setOrder(testOrder);
        testOffer.setProposedPrice(80.0);
        testOffer.setSpecialist(testSpecialist);
        testOffer.setProposedStartTime(LocalDateTime.now().minusHours(4));
        testOffer.setTimeToEndTheJobInHours(1);
    }

    @Test
    void existByServiceId() {
        assertFalse(orderService.existByServiceId(1L));
    }

    @Test
    void createOrder() {
        when(customerService.findById(anyLong())).thenReturn(Optional.of(customer));
        when(serviceService.findById(1L)).thenReturn(Optional.of(service));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OrderDto.OrderResponseDto result = orderService.createOrder(orderRequestDto, 1L);
        assertNotNull(result);
        assertEquals(120.0, result.getProposedPrice());
    }

    @Test
    void findAvailableOrdersForSpecialist() {
        when(specialistService.findById(anyLong())).thenReturn(Optional.of(specialist));
        when(orderRepository.findByOrderStatusInAndServiceIn(any(), any())).thenReturn(List.of(order));
        List<OrderDto.OrderResponseDto> result = orderService.findAvailableOrdersForSpecialist(1L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderDto.OrderResponseDto result = orderService.findOrderById(1L, "customer@gmail.com");
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void markWorkAsStarted() {
        order.getSelectedOffer().setProposedStartTime(LocalDateTime.now().minusHours(1));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.markWorkAsStarted(1L, 1L);
        assertEquals(OrderStatus.WORK_STARTED, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void markWorkAsDone() {
        order.setOrderStatus(OrderStatus.WORK_STARTED);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        orderService.markWorkAsDone(1L, 1L);
        assertEquals(OrderStatus.DONE, order.getOrderStatus());
        assertNotNull(order.getWorkCompletedDate());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void payForOrder(){
        testOrder.setOrderStatus(OrderStatus.DONE);
        testOrder.setSelectedOffer(testOffer);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(testOrder));
        assertEquals(100D, testCustomer.getWallet().getBalance());
        orderService.payForOrder(100L, 2L);
        verify(walletService).withdrawFromWallet(eq(testCustomer.getEmail()), eq(80.0));
        verify(walletService).depositToWallet(eq(testOffer.getSpecialist().getEmail()), eq(56.0));
        assertEquals(OrderStatus.PAID, testOrder.getOrderStatus());
    }

    @Test
    void getOrderHistoryForCustomer() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(customerService.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.findAllByCustomerId(1L, pageable)).thenReturn(orderPage);
        Page<OrderDto.CustomerOrderHistoryDto> result = orderService.getOrderHistoryForCustomer(1L, null, pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getOrderId()).isEqualTo(1L);
    }

    @Test
    void findOrdersHistoryForManager() {
        OrderFilterDto filter = new OrderFilterDto();
        filter.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(orderPage);
        Page<OrderDto.ManagerOrderHistorySummaryDto> result = orderService.getOrderHistoryForManager(filter, pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void getManagerOrderDetail(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewService.findByOrderId(1L)).thenReturn(Optional.of(review));
        OrderDto.ManagerOrderDetailDto detail = orderService.getManagerOrderDetail(1L);
        assertNotNull(detail);
        assertEquals(1L, detail.getId());
        assertEquals(3, detail.getReview().getRating());
    }

    @Test
    void getOrderHistoryForSpecialist_Success_WithReview() {
        Long specialistId = 2L;
        Pageable pageable = PageRequest.of(0, 10);
        Service service = new Service();
        service.setName("Gardening");
        testOrder.setService(service);
        Page<Offer> offerPage = new PageImpl<>(Collections.singletonList(testOffer), pageable, 1);
        when(specialistService.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(offerHelperService.findBySpecialistId(specialistId, pageable)).thenReturn(offerPage);
        Page<OrderDto.SpecialistOrderHistoryDto> result = orderService.getOrderHistoryForSpecialist(specialistId, pageable);
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        OrderDto.SpecialistOrderHistoryDto dto = result.getContent().getFirst();
        assertThat(dto.getOrderId()).isEqualTo(testOrder.getId());
        assertThat(dto.getServiceName()).isEqualTo(service.getName());
        assertThat(dto.getOrderStatus()).isEqualTo(testOrder.getOrderStatus());
        assertThat(dto.getYourProposedPrice()).isEqualTo(testOffer.getProposedPrice());
        verify(specialistService, times(1)).findById(specialistId);
        verify(offerHelperService, times(1)).findBySpecialistId(specialistId, pageable);
    }

    @Test
    void getOrderDetailForSpecialist() {
        Offer offerFromSpecialist = new Offer();
        offerFromSpecialist.setSpecialist(specialist);
        order.setOffers(Collections.singletonList(offerFromSpecialist));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(reviewService.findByOrderId(1L)).thenReturn(Optional.of(review));
        order.setSelectedOffer(offerFromSpecialist);
        OrderDto.SpecialistOrderDetailDto result = orderService.getOrderDetailForSpecialist(1L, 2L);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }
}