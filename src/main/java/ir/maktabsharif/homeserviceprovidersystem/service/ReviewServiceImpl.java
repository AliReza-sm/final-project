package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final SpecialistService specialistService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, OrderService orderService, SpecialistService specialistService) {
        super(reviewRepository);
        this.reviewRepository = reviewRepository;
        this.orderService = orderService;
        this.specialistService = specialistService;
    }


    @Override
    public Optional<Review> findByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId);
    }

    @Override
    public ReviewDto.ReviewResponseDto leaveReview(Long orderId, ReviewDto.ReviewRequestDto requestDto, Long customerId) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You are not the owner of this order.");
        }
        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new NotAllowedException("Order is not paid yet for this order.");
        }
        if (reviewRepository.findByOrderId(orderId).isPresent()) {
            throw new NotAllowedException("Order already have a review");
        }

        Review review = ReviewDto.mapToEntity(requestDto);
        review.setCustomer(order.getCustomer());
        review.setSpecialist(order.getSelectedOffer().getSpecialist());
        review.setOrder(order);

        Review savedReview = reviewRepository.save(review);
        updateSpecialistScore(order.getSelectedOffer().getSpecialist(), savedReview.getRating());

        return ReviewDto.mapToDto(savedReview);
    }

    private void updateSpecialistScore(Specialist specialist, Integer rating) {
        specialist.setSumScore(specialist.getSumScore() + rating);
        specialist.setNumberOfReviews(specialist.getNumberOfReviews() + 1);
        double average = specialist.getSumScore() / specialist.getNumberOfReviews();
        specialist.setAverageScore(average);
        specialistService.save(specialist);
    }

    @Override
    public Integer getRatingForAnOrder(Long orderId, Long specialistId){
        Review review = reviewRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with Order ID: " + orderId));
        if (!review.getSpecialist().getId().equals(specialistId)){
            throw new NotAllowedException("You are not the order's specialist");
        }
        return review.getRating();
    }
}
