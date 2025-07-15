package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;

import java.util.List;

public interface ServiceService {

    ServiceDto.ServiceResponseDto createService(ServiceDto.ServiceRequestDto requestDto);
    ServiceDto.ServiceResponseDto updateService(Long serviceId, ServiceDto.ServiceRequestDto requestDto);
    void deleteService(Long serviceId);
    ServiceDto.ServiceResponseDto findById(Long serviceId);
    List<ServiceDto.ServiceResponseDto> findAll();

}
