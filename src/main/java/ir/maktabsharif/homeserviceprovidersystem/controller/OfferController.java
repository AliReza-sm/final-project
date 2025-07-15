package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/offers")
@RequiredArgsConstructor

public class OfferController {

    private final OfferService offerService;

    @PostMapping("/{specialistId}")
    public ResponseEntity<OfferDto.OfferResponseDto> submitOffer(@PathVariable Long orderId, @PathVariable Long specialistId, @Valid @RequestBody OfferDto.OfferRequestDto requestDto) {
        requestDto.setOrderId(orderId);
        OfferDto.OfferResponseDto createdOffer = offerService.submitOffer(requestDto, specialistId);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<OfferDto.OfferResponseDto>> getOffersForOrder(
            @PathVariable Long orderId,
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "price") String sortBy) {
        List<OfferDto.OfferResponseDto> offers = offerService.getOffersForOrder(orderId, customerId, sortBy);
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/{offerId}/select/{customerId}")
    public ResponseEntity<Void> selectOffer(@PathVariable Long orderId, @PathVariable Long offerId, @PathVariable Long customerId) {
        offerService.selectOffer(orderId, offerId, customerId);
        return ResponseEntity.ok().build();
    }
}
