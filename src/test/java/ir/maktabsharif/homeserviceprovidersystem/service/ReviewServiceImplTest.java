package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ReviewDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private SpecialistService specialistService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Order order;
    private ReviewDto.ReviewRequestDto reviewRequestDto;
    private Review review;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");

        Specialist specialist = new Specialist();
        specialist.setId(1L);
        specialist.setEmail("specialist@gmail.com");

        Offer offer = new Offer();
        offer.setSpecialist(specialist);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.PAID);
        order.setSelectedOffer(offer);

        review = new Review();
        review.setId(1L);
        review.setOrder(order);
        review.setSpecialist(specialist);
        review.setRating(4);

        reviewRequestDto = new ReviewDto.ReviewRequestDto();
        reviewRequestDto.setRating(5);
        reviewRequestDto.setComment("good");
    }

    @Test
    void leaveReview() {
        when(orderService.findById(1L)).thenReturn(Optional.of(order));
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));
        when(specialistService.save(any(Specialist.class))).thenAnswer(inv -> inv.getArgument(0));
        ReviewDto.ReviewResponseDto result = reviewService.leaveReview(1L, reviewRequestDto, 1L);
        assertNotNull(result);
        assertEquals(5, result.getRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(specialistService, times(1)).save(any(Specialist.class));
    }

    @Test
    void getRatingForAnOrder(){
        when(reviewRepository.findByOrderId(1L)).thenReturn(Optional.of(review));
        Integer result = reviewService.getRatingForAnOrder(1L, 1L);
        assertEquals(4, result);
    }

}