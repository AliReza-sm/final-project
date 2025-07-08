package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import jakarta.validation.constraints.*;
import lombok.Data;

public class ReviewDto {

    @Data
    public static class ReviewResponseDto {
        private Long id;
        private Integer rating;
        private String comment;
        private Long orderId;
        private Long customerId;
        private Long specialistId;
    }

    @Data
    public static class ReviewRequestDto {
        @NotNull
        @Min(1)
        @Max(5)
        private Integer rating;
        private String comment;
        private Long orderId;
    }

    public static Review mapToEntity(ReviewRequestDto dto) {
        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return review;
    }

    public static ReviewResponseDto mapToDto(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setOrderId(review.getOrder().getId());
        dto.setCustomerId(review.getCustomer().getId());
        dto.setSpecialistId(review.getSpecialist().getId());
        return dto;
    }
}
