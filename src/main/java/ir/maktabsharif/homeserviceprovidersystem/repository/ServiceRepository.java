package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ServiceRepository extends CrudRepository<Service, Long> {

    Optional<Service> findByName(String name);

}
