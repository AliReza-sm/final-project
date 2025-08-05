package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.TemporaryEmailDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@Transactional
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long> implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TemporaryEmailService temporaryEmailService;
    private final EmailServiceImpl emailService;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, TemporaryEmailService temporaryEmailService, EmailServiceImpl emailService, VerificationTokenService verificationTokenService, PasswordEncoder passwordEncoder) {
        super(customerRepository);
        this.customerRepository = customerRepository;
        this.temporaryEmailService = temporaryEmailService;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public CustomerDto.CustomerResponseDto update(Long customerId, CustomerDto.CustomerUpdateDto dto){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (dto.getEmail() != null){
            temporaryEmailService.createTemporaryEmail(customerId, dto.getEmail());
            VerificationToken verificationToken = verificationTokenService.create(customer, VerificationTokenType.UPDATE);
            emailService.sendActivationEmail(dto.getEmail(), verificationToken.getToken(), verificationToken.getVerificationTokenType());
        }
        if (dto.getFirstName() != null){
            customer.setFirstname(dto.getFirstName());
        }
        if (dto.getLastName() != null){
            customer.setLastname(dto.getLastName());
        }
        if (dto.getPassword() != null){
            customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        Customer updateCustomer = customerRepository.save(customer);
        return CustomerDto.mapToDto(updateCustomer);
    }
}
