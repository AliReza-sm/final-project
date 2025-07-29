package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders/{orderId}/offers")
@RequiredArgsConstructor

public class OfferController {

    private final OfferService offerService;

    @PostMapping
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<OfferDto.OfferResponseDto> submitOffer(@PathVariable Long orderId,
                                                                 @AuthenticationPrincipal MyUserDetails userDetails,
                                                                 @Valid @RequestBody OfferDto.OfferRequestDto requestDto) {
        requestDto.setOrderId(orderId);
        OfferDto.OfferResponseDto createdOffer = offerService.submitOffer(requestDto, userDetails.getId());
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<OfferDto.OfferResponseDto>> getOffersForOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam(defaultValue = "price") String sortBy) {
        List<OfferDto.OfferResponseDto> offers = offerService.getOffersForOrder(orderId, userDetails.getId(), sortBy);
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/{offerId}/select")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Void> selectOffer(@PathVariable Long orderId,
                                            @PathVariable Long offerId,
                                            @AuthenticationPrincipal MyUserDetails userDetails) {
        offerService.selectOffer(orderId, offerId, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
