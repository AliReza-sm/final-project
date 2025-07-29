package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;

import java.util.List;

public interface OfferService extends BaseService<Offer, Long> {

    OfferDto.OfferResponseDto submitOffer(OfferDto.OfferRequestDto requestDto, Long offerId);
    void selectOffer(Long orderId, Long offerId, Long customerId);
    List<OfferDto.OfferResponseDto> getOffersForOrder(Long orderId, Long customerId, String sortBy);

}
