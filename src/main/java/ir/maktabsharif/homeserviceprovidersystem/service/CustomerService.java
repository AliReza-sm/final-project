package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;

public interface CustomerService extends BaseService<Customer, Long>{

    CustomerDto.CustomerResponseDto update(Long customerId, CustomerDto.CustomerUpdateDto dto);

}
