package ir.maktabsharif.homeserviceprovidersystem.service.Helper;

import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import ir.maktabsharif.homeserviceprovidersystem.service.ReviewService;
import ir.maktabsharif.homeserviceprovidersystem.service.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewHelperService {

    private final ReviewRepository reviewRepository;

    public Optional<Review> findByOrderId(Long orderId) {
        return reviewRepository.findByOrderId(orderId);
    }


}
