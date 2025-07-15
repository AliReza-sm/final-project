package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;

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

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setService(service);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
        order.setSelectedOffer(new Offer());

        orderRequestDto = new OrderDto.OrderRequestDto();
        orderRequestDto.setServiceId(1L);
        orderRequestDto.setProposedPrice(120.0);
        orderRequestDto.setAddress("eram");
        orderRequestDto.setProposedStartDate(LocalDateTime.now().plusDays(1));

        testCustomer = new Customer();
        testCustomer.setId(2L);
        testCustomer.setWallet(new Wallet());
        testCustomer.getWallet().setBalance(100D);

        Specialist testSpecialist = new Specialist();
        testSpecialist.setWallet(new Wallet());
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
    void createOrder() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OrderDto.OrderResponseDto result = orderService.createOrder(orderRequestDto, 1L);
        assertNotNull(result);
        assertEquals(120.0, result.getProposedPrice());
    }

    @Test
    void findAvailableOrdersForSpecialist() {
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
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
        when(transactionRepository.save(any())).thenReturn(new Transaction());
        when(walletRepository.save(any())).thenReturn(testCustomer.getWallet());
        assertEquals(100D, testCustomer.getWallet().getBalance());
        orderService.payForOrder(100L, 2L);
        assertEquals(20D, testCustomer.getWallet().getBalance());
        assertEquals(56D, testOffer.getSpecialist().getWallet().getBalance());
        assertEquals(OrderStatus.PAID, testOrder.getOrderStatus());
        assertEquals(20D, testCustomer.getWallet().getBalance());
        assertEquals(56D, testOffer.getSpecialist().getWallet().getBalance());
    }
}