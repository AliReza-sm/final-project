package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Review;

public class ReviewRepositoryImpl extends CrudRepositoryImpl<Review, Long> implements ReviewRepository {

    public ReviewRepositoryImpl() {
        super(Review.class);
    }

}
