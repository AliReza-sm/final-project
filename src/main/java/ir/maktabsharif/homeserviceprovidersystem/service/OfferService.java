package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;

import java.util.List;

public interface OfferService {

    OfferDto.OfferResponseDto submitOffer(OfferDto.OfferRequestDto requestDto, Long offerId);
    void selectOffer(Long orderId, Long offerId, Long customerId);
    List<OfferDto.OfferResponseDto> getOffersForOrder(Long orderId, Long customerId, String sortBy);

}
