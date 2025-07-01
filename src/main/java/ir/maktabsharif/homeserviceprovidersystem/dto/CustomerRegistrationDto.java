package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRegistrationDto(
        @NotBlank(message = "firstname can not be blank")
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        String firstname,
        @NotBlank(message = "lastname can not be blank")
        @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
        String lastname,
        @NotBlank(message = "email can not be blank")
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Invalid email format")
        String email,
        @NotBlank(message = "password can not be blank")
        @Pattern(regexp = "^[A-Za-z0-9]{8}$", message = "password must be at least 8 character or number")
        String password
) {
}
