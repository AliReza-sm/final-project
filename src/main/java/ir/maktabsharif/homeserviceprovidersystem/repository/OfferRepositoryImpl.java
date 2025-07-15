//package ir.maktabsharif.homeserviceprovidersystem.repository;
//
//import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
//import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
//import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
//import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
//import jakarta.persistence.EntityManager;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public class OfferRepositoryImpl extends CrudRepositoryImpl<Offer, Long> implements OfferRepository {
//
//    public OfferRepositoryImpl() {
//        super(Offer.class);
//    }
//
//
//    @Override
//    public Optional<Offer> findBySpecialistAndOfferStatus(Specialist specialist, OfferStatus offerStatus) {
//        return entityManager.createQuery("select o from Offer o where o.specialist = :specialist and o.offerStatus = :offerStatus", Offer.class)
//                .setParameter("specialist", specialist)
//                .setParameter("offerStatus", offerStatus)
//                .getResultList().stream().findFirst();
//    }
//}
