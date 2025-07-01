package ir.maktabsharif.homeserviceprovidersystem.dto;

import java.time.LocalDateTime;

public record CustomerResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime registrationDate
) {
}
