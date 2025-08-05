package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;

import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@org.springframework.stereotype.Service
@Transactional
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerService customerService;
    private final WalletService walletService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CustomerService customerService,
                              WalletService walletService) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.customerService = customerService;
        this.walletService = walletService;
    }

    @Override
    public PaymentDto.paymentStartDto startPayment(Long customerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Payment payment = paymentRepository.findByCustomerId(customerId)
                .orElse(new Payment());
        String captcha = generateCaptcha();
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(10);
        payment.setCustomer(customer);
        payment.setCaptchaCode(captcha);
        payment.setExpiresAt(expireTime);
        paymentRepository.save(payment);
        PaymentDto.paymentStartDto paymentStartDto = new PaymentDto.paymentStartDto();
        paymentStartDto.setCaptchaText(captcha);
        paymentStartDto.setExpiresAt(expireTime);
        return paymentStartDto;
    }

    private String generateCaptcha() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public PaymentDto.PaymentRequestDto processPayment(PaymentDto.PaymentRequestDto request) {
        Payment payment = paymentRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for this customer. Please start again."));

        if (LocalDateTime.now().isAfter(payment.getExpiresAt())) {
            paymentRepository.delete(payment);
            throw new NotAllowedException("Payment session has expired. Please try again.");
        }
        if (!payment.getCaptchaCode().equals(request.getCaptchaInput())) {
            throw new NotAllowedException("CAPTCHA is incorrect.");
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new NotAllowedException("Payment amount must be a positive value.");
        }

        Customer customer = customerService.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        walletService.depositToWallet(customer.getEmail(), request.getAmount());
        paymentRepository.delete(payment);
        return request;
    }

    @Override
    public URL getPaymentPage (Long customerId) throws MalformedURLException {
        return new URL("http://localhost:8081/payment.html?customerId="+customerId);
    }
}
