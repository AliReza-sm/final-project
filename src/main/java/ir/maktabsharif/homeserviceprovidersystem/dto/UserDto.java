package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import jakarta.validation.constraints.*;
import lombok.Data;

public class UserDto {

    @Data
    public static class LoginRequestDto {
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        private String password;
    }

    @Data
    public static class LoginResponseDto {
        private Long id;
        private String email;
        private String role;
    }

    @Data
    public static class UserRegistrationDto {
        @NotBlank(message = "firstname can not be blank")
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        private String firstName;
        @NotBlank(message = "lastname can not be blank")
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        private String lastName;
        @NotBlank(message = "email can not be blank")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Invalid email format")
        private String email;
        @NotBlank(message = "password can not be blank")
        @Pattern(regexp = "^[A-Za-z0-9]{8,}$", message = "password must be at least 8 character or number")
        private String password;
    }

    @Data
    public static class UserResponseDto {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
    }

    public static UserDto.UserResponseDto mapToUserResponseDto(User user) {
        UserDto.UserResponseDto dto = new UserDto.UserResponseDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstname());
        dto.setLastName(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
