package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Manager;

public class ManagerRepositoryImpl extends UserRepositoryImpl<Manager> implements ManagerRepository {

    public ManagerRepositoryImpl() {
        super(Manager.class);
    }
}
