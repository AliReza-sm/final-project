package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;

public class CustomerRepositoryImpl extends UserRepositoryImpl<Customer> implements CustomerRepository {

    public CustomerRepositoryImpl() {
        super(Customer.class);
    }
}
