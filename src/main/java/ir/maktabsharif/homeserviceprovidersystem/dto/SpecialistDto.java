package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.AccountStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
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
    @Setter
    @Getter
    public static class SpecialistRequestDto extends UserDto.UserRegistrationDto{
        private MultipartFile profilePhotoData;
        @Max(value = 300000, message = "file must be at most 300KB")
        private Long fileSize;

        public SpecialistRequestDto() {
        }

        public SpecialistRequestDto(MultipartFile profilePhotoData) {
            this.profilePhotoData = profilePhotoData;
            if (profilePhotoData != null) {
                this.fileSize = profilePhotoData.getSize();
            }
        }

        public void setProfilePhotoData(@NotNull MultipartFile profilePhotoData) {
            this.profilePhotoData = profilePhotoData;
            if (profilePhotoData != null) {
                this.fileSize = profilePhotoData.getSize();
            }
        }
    }

    @Data
    public static class SpecialistUpdateDto{
        private String email;
        private String password;
        private MultipartFile profilePhotoData;
        @Max(value = 300000, message = "file must be at most 300KB")
        private Long fileSize;
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
