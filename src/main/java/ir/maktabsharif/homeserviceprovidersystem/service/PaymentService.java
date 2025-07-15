package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;

public interface PaymentService {

    PaymentDto.paymentStartDto startPayment(Long customerId);
    void processPayment(PaymentDto.PaymentRequestDto request);

}
