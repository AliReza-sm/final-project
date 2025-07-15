package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private WalletRepository walletRepository;


    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Payment payment;
    private PaymentDto.PaymentRequestDto paymentRequestDto;
    private Specialist specialist;
    private Customer customer;

    @BeforeEach
    void setUp() {
        Wallet customerWallet = new Wallet();
        customerWallet.setBalance(500.0);
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");
        customer.setWallet(customerWallet);

        Wallet specialistWallet = new Wallet();
        specialistWallet.setBalance(0.0);
        specialist = new Specialist();
        specialist.setWallet(specialistWallet);

        Offer offer = new Offer();
        offer.setSpecialist(specialist);
        offer.setProposedPrice(200.0);
        offer.setProposedStartTime(LocalDateTime.now().minusHours(1));

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.DONE);
        order.setSelectedOffer(offer);
        order.setWorkCompletedDate(LocalDateTime.now());

        payment = new Payment();
        payment.setCaptchaCode("123456");
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        paymentRequestDto = new PaymentDto.PaymentRequestDto();
        paymentRequestDto.setCustomerId(1L);
        paymentRequestDto.setCaptchaInput("123456");
        paymentRequestDto.setAmount(200.0);
    }

    @Test
    void startPayment() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(paymentRepository.findByCustomerId(1L)).thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());
        PaymentDto.paymentStartDto result = paymentService.startPayment( 1L);
        assertNotNull(result);
        assertNotNull(result.getCaptchaText());
        assertTrue(result.getExpiresAt().isAfter(LocalDateTime.now()));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void processPayment() {
        when(paymentRepository.findByCustomerId(1L)).thenReturn(Optional.of(payment));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(walletRepository.save(any(Wallet.class))).thenReturn(new Wallet());
        specialistRepository.save(specialist);
        paymentService.processPayment(paymentRequestDto);
        verify(paymentRepository, times(1)).delete(payment);
    }
}