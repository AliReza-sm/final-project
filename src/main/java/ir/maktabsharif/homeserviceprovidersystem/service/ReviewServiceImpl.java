package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor

public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final SpecialistRepository specialistRepository;


    @Override
    public ReviewDto.ReviewResponseDto leaveReview(Long orderId, ReviewDto.ReviewRequestDto requestDto, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You are not the owner of this order.");
        }
        if (order.getOrderStatus() != OrderStatus.PAID) {
            throw new NotAllowedException("Order is not paid yet for this order.");
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
        specialistRepository.save(specialist);
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
