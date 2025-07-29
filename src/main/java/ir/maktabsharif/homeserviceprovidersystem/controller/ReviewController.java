package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/reviews")
@RequiredArgsConstructor

public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ReviewDto.ReviewResponseDto> leaveReview(
            @PathVariable Long orderId,
            @AuthenticationPrincipal MyUserDetails userDetails,
            @Valid @RequestBody ReviewDto.ReviewRequestDto requestDto) {
        ReviewDto.ReviewResponseDto createdReview = reviewService.leaveReview(orderId, requestDto, userDetails.getId());
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<Integer> getRating(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(reviewService.getRatingForAnOrder(orderId, userDetails.getId()));
    }

}
