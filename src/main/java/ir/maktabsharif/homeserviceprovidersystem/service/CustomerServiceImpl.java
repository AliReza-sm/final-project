package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    public CustomerDto.CustomerResponseDto update(Long customerId, CustomerDto.CustomerUpdateDto dto){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (dto.getEmail() != null){
            if (customerRepository.findByEmail(dto.getEmail()).isPresent()){
                throw new AlreadyExistException("email already exist");
            }
            customer.setEmail(dto.getEmail());
        }
        if (dto.getFirstName() != null){
            customer.setFirstname(dto.getFirstName());
        }
        if (dto.getLastName() != null){
            customer.setLastname(dto.getLastName());
        }
        if (dto.getPassword() != null){
            customer.setPassword(dto.getPassword());
        }
        Customer updateCustomer = customerRepository.save(customer);
        return CustomerDto.mapToDto(updateCustomer);
    }
}
