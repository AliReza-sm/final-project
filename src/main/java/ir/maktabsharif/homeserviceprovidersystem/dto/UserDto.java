package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class UserDto {

    @Data
    public static class LoginRequestDto {
        private String email;
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
}
