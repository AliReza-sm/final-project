package ir.maktabsharif.homeserviceprovidersystem.service.Helper;

import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import ir.maktabsharif.homeserviceprovidersystem.service.SpecialistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialistHelperService {

    private final SpecialistRepository specialistRepository;

    public Specialist save(Specialist specialist) {
        return specialistRepository.save(specialist);
    }

    public Optional<Specialist> findById(Long id) {
        return specialistRepository.findById(id);
    }

    public Optional<Specialist> findByEmail(String email) {
        return specialistRepository.findByEmail(email);
    }
}
