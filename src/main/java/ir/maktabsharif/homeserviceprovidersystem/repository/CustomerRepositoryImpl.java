package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryImpl extends UserRepositoryImpl<Customer> implements CustomerRepository {

    public CustomerRepositoryImpl() {
        super(Customer.class);
    }
}
