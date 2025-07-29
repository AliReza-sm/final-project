package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

public class CustomerDto {

    @Data
    public static class CustomerResponseDto{
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDateTime registrationDate;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class CustomerRequestDto extends UserDto.UserRegistrationDto{

    }

    @Data
    public static class CustomerUpdateDto{
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        private String firstName;
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        private String lastName;
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Invalid email format")
        private String email;
        @Pattern(regexp = "^[A-Za-z0-9]{8,}$", message = "password must be at least 8 character or number")
        private String password;
    }

    public static CustomerResponseDto mapToDto(Customer customer){
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstname());
        dto.setLastName(customer.getLastname());
        dto.setRegistrationDate(customer.getRegistrationDate());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public static Customer mapToEntity(CustomerRequestDto dto){
        Customer customer = new Customer();
        customer.setFirstname(dto.getFirstName());
        customer.setLastname(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(dto.getPassword());
        return customer;
    }
}
