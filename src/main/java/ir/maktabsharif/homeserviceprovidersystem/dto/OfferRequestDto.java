package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record OfferRequestDto(
        @NotNull
        Long orderId,
        @NotNull
        @Positive
        Double proposedPrice,
        @NotNull
        @Positive
        Integer durationInHours,
        @Future
        @NotNull
        LocalDateTime proposedStartTime
) {
}
