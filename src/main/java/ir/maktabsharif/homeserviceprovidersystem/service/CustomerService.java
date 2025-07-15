package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;

public interface CustomerService {

    CustomerDto.CustomerResponseDto register(CustomerDto.CustomerRequestDto dto);
    CustomerDto.CustomerResponseDto update(Long customerId, CustomerDto.CustomerUpdateDto dto);

}
