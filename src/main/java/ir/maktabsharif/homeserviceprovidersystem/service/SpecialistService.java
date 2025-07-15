package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;

import java.io.IOException;
import java.util.List;

public interface SpecialistService {

    SpecialistDto.SpecialistResponseDto register(SpecialistDto.SpecialistRequestDto dto) throws IOException;
    void updateSpecialist(Long specialistId, SpecialistDto.SpecialistUpdateDto dto) throws IOException;
    List<SpecialistDto.SpecialistResponseDto> findAllSpecialists();
    void approveSpecialist(Long specialistId);
    void assignSpecialistToService(Long specialistId, Long serviceId);
    void removeSpecialistFromService(Long specialistId, Long serviceId);
    List<SpecialistDto.SpecialistOrderHistoryDto> getOrderHistory(Long specialistId);
    Double getAverageScore(Long specialistId);
}
