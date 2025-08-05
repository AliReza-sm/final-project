package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SpecialistService extends BaseService<Specialist, Long> {

    Optional<Specialist> findByEmail(String email);
    void updateSpecialist(Long specialistId, SpecialistDto.SpecialistUpdateDto dto) throws IOException;
    void updateSpecialistPhoto(Long specialistId, MultipartFile photo) throws IOException;
    List<SpecialistDto.SpecialistResponseDto> findAllSpecialists();
    void approveSpecialist(Long specialistId);
    void assignSpecialistToService(Long specialistId, Long serviceId);
    void removeSpecialistFromService(Long specialistId, Long serviceId);
    SpecialistDto.SpecialistRating getAverageScore(Long specialistId);
}
