package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ServiceRepositoryImpl extends CrudRepositoryImpl<Service, Long> implements ServiceRepository {


    protected ServiceRepositoryImpl() {
        super(Service.class);
    }

    @Override
    public Optional<Service> findByName(String name) {
        return entityManager.createQuery("select s from Service s where s.name = :name", Service.class)
                .setParameter("name", name)
                .getResultList().stream().findFirst();
    }

}
