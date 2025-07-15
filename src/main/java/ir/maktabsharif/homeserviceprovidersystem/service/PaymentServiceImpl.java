package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto.paymentStartDto startPayment(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
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
    public void processPayment(PaymentDto.PaymentRequestDto request) {
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

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        pay(customer, request.getAmount());
        paymentRepository.delete(payment);
    }

    private void pay(Customer customer, Double amount) {

        Wallet customerWallet = customer.getWallet();
        customerWallet.setBalance(customerWallet.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setWallet(customerWallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription("Deposit to customer wallet with id: " + customer.getId());
        transactionRepository.save(transaction);
        walletRepository.save(customerWallet);
    }
}




//        Offer acceptedOffer = order.getSelectedOffer();
//        if (acceptedOffer == null) {
//            throw new NotAllowedException("No offer has been selected for this order.");
//        }
//        if (order.getWorkCompletedDate() == null) {
//            throw new NotAllowedException("Order completion time is null");
//        }
//
//        long delayInHours = Duration.between(acceptedOffer.getProposedStartTime(), order.getWorkCompletedDate()).toHours();
//        Specialist specialist = acceptedOffer.getSpecialist();
//        if (delayInHours > 0) {
//            specialist.setSumScore(specialist.getSumScore() - delayInHours);
//            if (specialist.getSumScore() < 0) specialist.setSumScore(0.0);
//            if (specialist.getNumberOfReviews() > 0) {
//                specialist.setAverageScore(specialist.getSumScore() / specialist.getNumberOfReviews());
//            } else {
//                specialist.setAverageScore(0.0);
//            }
//            specialist.setSpecialistStatus(SpecialistStatus.INACTIVE);
//            specialistRepository.save(specialist);
//        }

//        Wallet specialistWallet = specialist.getWallet();
//        customerWallet.setBalance(customerWallet.getBalance() - amount);
//        specialistWallet.setBalance(specialistWallet.getBalance() + (amount * 0.7));

//        walletRepository.save(specialistWallet);
//        order.setOrderStatus(OrderStatus.PAID);
//        orderRepository.save(order);
