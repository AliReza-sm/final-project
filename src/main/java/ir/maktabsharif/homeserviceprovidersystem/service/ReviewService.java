package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Review;

import java.util.Optional;

public interface ReviewService extends BaseService<Review, Long>{

    Optional<Review> findByOrderId(Long orderId);
    ReviewDto.ReviewResponseDto leaveReview(Long orderId, ReviewDto.ReviewRequestDto requestDto, Long customerId);
    Integer getRatingForAnOrder(Long orderId, Long specialistId);
}
