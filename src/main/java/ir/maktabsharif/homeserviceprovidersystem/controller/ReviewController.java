package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/{orderId}/reviews")
@RequiredArgsConstructor

public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{customerId}")
    public ResponseEntity<ReviewDto.ReviewResponseDto> leaveReview(
            @PathVariable Long orderId,
            @PathVariable Long customerId,
            @Valid @RequestBody ReviewDto.ReviewRequestDto requestDto) {
        ReviewDto.ReviewResponseDto createdReview = reviewService.leaveReview(orderId, requestDto, customerId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @GetMapping("/{specialistId}")
    public ResponseEntity<Integer> getRating(@PathVariable Long orderId, @PathVariable Long specialistId) {
        return ResponseEntity.ok(reviewService.getRatingForAnOrder(orderId, specialistId));
    }
}
