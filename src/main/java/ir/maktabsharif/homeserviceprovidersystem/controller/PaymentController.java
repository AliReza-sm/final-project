package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/start/{customerId}")
    public ResponseEntity<PaymentDto.paymentStartDto> startPayment(
            @PathVariable Long customerId) {
        PaymentDto.paymentStartDto response = paymentService.startPayment(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentDto.PaymentRequestDto> processPayment(@RequestBody PaymentDto.PaymentRequestDto request) {
        paymentService.processPayment(request);
        return ResponseEntity.ok(request);
    }
}
