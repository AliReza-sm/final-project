package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.PaymentDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/start")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<PaymentDto.paymentStartDto> startPayment(
            @AuthenticationPrincipal MyUserDetails userDetails) {
        PaymentDto.paymentStartDto response = paymentService.startPayment(userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentDto.PaymentRequestDto> processPayment(@RequestBody PaymentDto.PaymentRequestDto request) {
        paymentService.processPayment(request);
        return ResponseEntity.ok(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<URL> getPaymentPage (@AuthenticationPrincipal MyUserDetails userDetails) throws MalformedURLException {
        return ResponseEntity.ok(paymentService.getPaymentPage(userDetails.getId()));
    }
}
