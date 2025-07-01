package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;

import java.time.LocalDateTime;

public record OfferResponseDto(
        Long id,
        Order order,
        Double proposedPrice,
        Integer timeToEndTheJobInHours,
        LocalDateTime proposedStartTime,
        LocalDateTime submissionTime,
        OfferStatus offerStatus,
        Specialist specialist
) {
}
