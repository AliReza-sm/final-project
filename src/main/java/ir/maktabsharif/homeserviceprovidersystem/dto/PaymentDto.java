package ir.maktabsharif.homeserviceprovidersystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class PaymentDto {

    @Data
    public static class PaymentRequestDto {
        private Long customerId;
        private Double amount;
        private String cardNumber;
        private String password;
        private String expiryDate;
        private String cvv;
        private String captchaInput;
    }

    @Data
    public static class paymentStartDto {
        private String captchaText;
        private LocalDateTime expiresAt;
    }

    @Data
    @AllArgsConstructor
    public static class paymentMessage {
        private String message;
    }
}
