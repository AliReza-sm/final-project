package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private LoginServiceImpl loginService;
    private UserDto.LoginRequestDto loginRequestDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        loginRequestDto = new UserDto.LoginRequestDto();
        loginRequestDto.setEmail("ali@google.com");
        loginRequestDto.setPassword("password");

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("ali@google.com");
        customer.setPassword("password");
        customer.setRole(Role.CUSTOMER);
    }

    @Test
    void Login_Customer() {
        when(managerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(specialistRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("ali@google.com")).thenReturn(Optional.of(customer));
        UserDto.LoginResponseDto result = loginService.login(loginRequestDto);
        assertNotNull(result);
        assertEquals("ali@google.com", result.getEmail());
        assertEquals("customer", result.getRole());
    }
}