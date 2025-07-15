package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends BaseRepository<Offer, Long> {

    boolean existsBySpecialistAndOfferStatus(Specialist specialist, OfferStatus offerStatus);
    List<Offer> findBySpecialist(Specialist specialist);

}
