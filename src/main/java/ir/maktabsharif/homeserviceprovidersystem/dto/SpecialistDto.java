package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.AccountStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class SpecialistDto {

    @Data
    public static class SpecialistResponseDto{
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private AccountStatus accountStatus;
        private Double averageScore;
        private LocalDateTime registerDate;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SpecialistRequestDto extends UserDto.UserRegistrationDto{
        private MultipartFile profilePhotoData;
    }

    @Data
    public static class SpecialistUpdateDto{
        @Email(message = "email format is wrong")
        private String email;
        @Pattern(regexp = "^[A-Za-z0-9]{8,}$", message = "password must be at least 8 character or number")
        private String password;
    }

    @Data
    public static class SpecialistPhotoUpdateDto{
        private MultipartFile profilePhotoData;
    }

    @Data
    public static class SpecialistRating{
        private double averageScore;
    }

    public static SpecialistResponseDto mapToDto(Specialist specialist){
        if(specialist == null) return null;
        SpecialistResponseDto specialistResponseDto = new SpecialistResponseDto();
        specialistResponseDto.setId(specialist.getId());
        specialistResponseDto.setFirstName(specialist.getFirstname());
        specialistResponseDto.setLastName(specialist.getLastname());
        specialistResponseDto.setEmail(specialist.getEmail());
        specialistResponseDto.setAverageScore(specialist.getAverageScore());
        specialistResponseDto.setAccountStatus(specialist.getAccountStatus());
        specialistResponseDto.setRegisterDate(specialist.getRegistrationDate());
        return specialistResponseDto;
    }
}
