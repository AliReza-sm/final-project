package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;

public interface ReviewService {

    ReviewDto.ReviewResponseDto leaveReview(Long orderId, ReviewDto.ReviewRequestDto requestDto, Long customerId);
    Integer getRatingForAnOrder(Long orderId, Long specialistId);

}
