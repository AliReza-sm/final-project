package ir.maktabsharif.homeserviceprovidersystem.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record SpecialistResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String status,
        Double sumScore,
        Integer numberOfReviews,
        Double averageScore,
        byte[] profilePhotoBytes,
        LocalDateTime registrationDate
) {
}
