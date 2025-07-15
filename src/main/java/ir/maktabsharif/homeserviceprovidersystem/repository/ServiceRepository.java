package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends BaseRepository<Service, Long> {

    Optional<Service> findByName(String name);

}
