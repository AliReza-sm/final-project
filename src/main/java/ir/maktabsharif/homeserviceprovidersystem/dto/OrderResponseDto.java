package ir.maktabsharif.homeserviceprovidersystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Double proposedPrice,
        String description,
        String address,
        LocalDateTime proposedStartDate,
        String status,
        ServiceResponseDto service,
        CustomerResponseDto customer,
        List<OfferResponseDto> offers
) {
}
