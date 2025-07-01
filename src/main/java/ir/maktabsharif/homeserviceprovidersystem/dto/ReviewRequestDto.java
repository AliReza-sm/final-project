package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.*;

public record ReviewRequestDto (
        @NotNull
        @Min(1)
        @Max(5)
        Integer rating,
        String comment,
        Long orderId
) {
}
