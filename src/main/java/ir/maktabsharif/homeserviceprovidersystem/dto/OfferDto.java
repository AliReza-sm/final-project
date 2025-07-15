package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

public class OfferDto {

    @Data
    public static class OfferResponseDto{
        private Long id;
        private Double proposedPrice;
        private Integer TimeToEndTheJobInHours;
        private LocalDateTime proposedStartTime;
        private OfferStatus status;
        private SpecialistDto.SpecialistResponseDto specialist;
    }

    @Data
    public static class OfferRequestDto {
        @NotNull(message = "Order ID cannot be null")
        private Long orderId;
        @NotNull(message = "Proposed price cannot be null")
        @Positive
        private Double proposedPrice;
        @NotNull(message = "Time to end the job cannot be null")
        @Positive
        private Integer TimeToEndTheJobInHours;
        @NotNull(message = "Proposed start time cannot be null")
        @Future
        private LocalDateTime proposedStartTime;
    }

    public static OfferResponseDto mapToDto(Offer offer) {
        if (offer == null) return null;
        OfferResponseDto dto = new OfferResponseDto();
        dto.setId(offer.getId());
        dto.setProposedPrice(offer.getProposedPrice());
        dto.setTimeToEndTheJobInHours(offer.getTimeToEndTheJobInHours());
        dto.setProposedStartTime(offer.getProposedStartTime());
        dto.setStatus(offer.getOfferStatus());
        dto.setSpecialist(SpecialistDto.mapToDto(offer.getSpecialist()));
        return dto;
    }

    public static Offer mapToEntity(OfferRequestDto dto) {
        Offer offer = new Offer();
        offer.setProposedPrice(dto.getProposedPrice());
        offer.setTimeToEndTheJobInHours(dto.getTimeToEndTheJobInHours());
        offer.setProposedStartTime(dto.getProposedStartTime());
        return offer;
    }
}
