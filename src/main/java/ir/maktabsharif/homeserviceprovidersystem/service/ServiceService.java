package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;

import java.util.List;

public interface ServiceService extends BaseService<Service, Long> {

    ServiceDto.ServiceResponseDto createService(ServiceDto.ServiceRequestDto requestDto);
    ServiceDto.ServiceResponseDto updateService(Long serviceId, ServiceDto.ServiceRequestDto requestDto);
    void deleteService(Long serviceId);
    ServiceDto.ServiceResponseDto findServiceById(Long serviceId);
    List<ServiceDto.ServiceResponseDto> findAllServices();

}
