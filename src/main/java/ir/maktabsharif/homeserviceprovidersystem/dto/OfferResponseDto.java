package ir.maktabsharif.homeserviceprovidersystem.dto;

import java.time.LocalDateTime;

public record OfferResponseDto(
        Long id,
        OrderResponseDto order,
        Double proposedPrice,
        Integer durationInHours,
        LocalDateTime proposedStartTime,
        String status,
        SpecialistResponseDto specialist
) {
}
