package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Manager;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ManagerRepositoryImpl extends UserRepositoryImpl<Manager> implements ManagerRepository {

    public ManagerRepositoryImpl() {
        super(Manager.class);
    }
}
