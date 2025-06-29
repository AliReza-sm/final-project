package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;

import java.util.Optional;

public class ServiceRepositoryImpl extends CrudRepositoryImpl<Service, Long> implements ServiceRepository {


    protected ServiceRepositoryImpl() {
        super(Service.class);
    }

    @Override
    public Optional<Service> findByName(String name) {
        try {
            return Optional.ofNullable(entityManager.createQuery("select s from Service s where s.name = :name", Service.class)
                    .setParameter("name", name)
                    .getSingleResult());
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Service with name " + name + " not found");
        }
    }

}
