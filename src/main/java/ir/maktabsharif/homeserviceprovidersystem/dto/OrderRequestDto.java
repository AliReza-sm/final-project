package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record OrderRequestDto(
        @NotNull
        Long serviceId,
        @NotNull
        @Positive
        Double proposedPrice,
        String description,
        @NotBlank
        String address,
        @Future
        @NotNull
        LocalDateTime proposedStartDate

) {
}
