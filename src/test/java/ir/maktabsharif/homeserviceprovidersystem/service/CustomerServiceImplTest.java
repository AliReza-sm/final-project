package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;
    private Customer customer;
    private CustomerDto.CustomerRequestDto customerRequestDto;

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
    }

    @Test
    void register () {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        CustomerDto.CustomerResponseDto result = customerService.register(customerRequestDto);
        assertNotNull(result);
        assertEquals("ali@google.com", result.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
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
        customerService.update(1L, dto);
        verify(customerRepository, times(1)).save(customer);
        assertEquals("a", customer.getFirstname());
    }

}