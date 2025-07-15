package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.UserFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagerServiceImplTest {

    @Mock
    private UserRepository<User> userRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @Test
    void testFilterUsers() {

        UserFilterDto filterDto = new UserFilterDto();
        filterDto.setRole(Role.CUSTOMER);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("ali");
        customer.setLastname("reza");
        customer.setEmail("ali@gmail.com");
        customer.setRole(Role.CUSTOMER);

        when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(customer));
        List<UserDto.UserResponseDto> results = managerService.filterUsers(filterDto);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("ali@gmail.com", results.getFirst().getEmail());
        assertEquals("CUSTOMER", results.getFirst().getRole());
    }
}