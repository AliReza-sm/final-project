package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.TemporaryEmailDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.TemporaryEmail;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationToken;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationTokenType;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VerificationTokenServiceImpl verificationTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TemporaryEmailService temporaryEmailService;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private Customer customer;
    private CustomerDto.CustomerRequestDto customerRequestDto;
    private VerificationToken verificationToken;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("ali");
        customer.setLastname("reza");
        customer.setEmail("ali@google.com");
        customer.setPassword("password123");

        customerRequestDto = new CustomerDto.CustomerRequestDto();
        customerRequestDto.setFirstName("ali");
        customerRequestDto.setLastName("reza");
        customerRequestDto.setEmail("ali@google.com");
        customerRequestDto.setPassword("password123");

        verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken("aa");
        verificationToken.setVerificationTokenType(VerificationTokenType.UPDATE);
    }

    @Test
    void update(){
        CustomerDto.CustomerUpdateDto dto = new CustomerDto.CustomerUpdateDto();
        dto.setEmail("a@mail.com");
        dto.setPassword("AaBbCc11");
        dto.setFirstName("a");
        dto.setLastName("a");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);
        when(verificationTokenService.create(any(), any())).thenReturn(verificationToken);
        when(temporaryEmailService.createTemporaryEmail(1L, dto.getEmail())).thenReturn(new TemporaryEmailDto.TemporaryEmailResponse(customer.getEmail(), dto.getEmail()));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        customerService.update(1L, dto);
        verify(customerRepository, times(1)).save(customer);
        assertEquals("a", customer.getFirstname());
    }

}