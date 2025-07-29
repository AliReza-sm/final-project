package ir.maktabsharif.homeserviceprovidersystem.service.Helper;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.OfferStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferHelperService {

    private final OfferRepository offerRepository;

    public boolean existsBySpecialistAndOfferStatus(Specialist specialist, OfferStatus offerStatus) {
        return offerRepository.existsBySpecialistAndOfferStatus(specialist, offerStatus);
    }

    public Page<Offer> findBySpecialistId(Long specialistId, Pageable pageable) {
        return offerRepository.findBySpecialistId(specialistId, pageable);
    }
}
