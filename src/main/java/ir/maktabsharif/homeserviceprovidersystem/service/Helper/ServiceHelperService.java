package ir.maktabsharif.homeserviceprovidersystem.service.Helper;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceHelperService {

    private final ServiceRepository serviceRepository;

    public Optional<Service> findById(Long id) {
        return serviceRepository.findById(id);
    }
}
