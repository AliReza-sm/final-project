package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface OfferRepository extends CrudRepository<Offer, Long> {

    Optional<Offer> findBySpecialistAndOfferStatus(Specialist specialist, OfferStatus offerStatus);

}
