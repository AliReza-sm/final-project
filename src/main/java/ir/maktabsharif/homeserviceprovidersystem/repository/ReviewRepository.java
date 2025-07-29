package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends BaseRepository<Review, Long> {

    Optional<Review> findByOrderId(Long orderId);
    @Query("select avg(r.rating) from Review r where r.order.selectedOffer.specialist.id = :specialistId")
    Double findAverageRating(@Param("specialistId") Long specialistId);

}
