package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ServiceRequestDto(
        @NotBlank(message = "service name can not be blank")
        String name,
        String description,
        @Positive(message = "base price must be positive")
        Double basePrice,
        Long parentServiceId
) {
}
