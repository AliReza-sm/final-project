package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends CrudRepositoryImpl<Review, Long> implements ReviewRepository {

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

}
