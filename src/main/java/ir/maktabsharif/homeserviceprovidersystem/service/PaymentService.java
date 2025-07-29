package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Payment;
import ir.maktabsharif.homeserviceprovidersystem.repository.PaymentRepository;

import java.net.MalformedURLException;
import java.net.URL;

public interface PaymentService extends BaseService<Payment, Long> {

    PaymentDto.paymentStartDto startPayment(Long customerId);
    void processPayment(PaymentDto.PaymentRequestDto request);
    URL getPaymentPage(Long customerId) throws MalformedURLException;
}
