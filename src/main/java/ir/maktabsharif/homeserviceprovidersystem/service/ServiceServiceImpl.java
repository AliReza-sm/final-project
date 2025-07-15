package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional

public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;

    @Override
    public ServiceDto.ServiceResponseDto createService(ServiceDto.ServiceRequestDto requestDto) {
        if (serviceRepository.findByName(requestDto.getName()).isPresent()) {
            throw new AlreadyExistException("Service with name '" + requestDto.getName() + "' already exists.");
        }

        Service service = ServiceDto.mapToEntity(requestDto);

        if (requestDto.getParentServiceId() != null) {
            Service parent = serviceRepository.findById(requestDto.getParentServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent service not found with ID: " + requestDto.getParentServiceId()));
            service.setParentService(parent);
        }

        Service savedService = serviceRepository.save(service);
        return ServiceDto.mapToDto(savedService);
    }

    @Override
    public ServiceDto.ServiceResponseDto updateService(Long serviceId, ServiceDto.ServiceRequestDto requestDto) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        if (requestDto.getName() != null && !service.getName().equals(requestDto.getName())) {
            serviceRepository.findByName(requestDto.getName()).ifPresent(s -> {
                throw new AlreadyExistException("Service with name '" + requestDto.getName() + "' already exists.");
            });
            service.setName(requestDto.getName());
        }

        if (requestDto.getDescription() != null) {
            service.setDescription(requestDto.getDescription());
        }
        if (requestDto.getBasePrice() != null) {
            service.setBasePrice(requestDto.getBasePrice());
        }

        Service updatedService = serviceRepository.save(service);
        return ServiceDto.mapToDto(updatedService);
    }

    @Override
    public void deleteService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            throw new ResourceNotFoundException("Service not found with ID: " + serviceId);
        }
        if (orderRepository.existsByServiceId(serviceId)) {
            throw new NotAllowedException("Cannot delete service with ID " + serviceId + " as it has active orders.");
        }
        serviceRepository.deleteById(serviceId);
    }

    @Override
    public ServiceDto.ServiceResponseDto findById(Long serviceId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));
        return ServiceDto.mapToDto(service);
    }

    @Override
    public List<ServiceDto.ServiceResponseDto> findAll() {
        return serviceRepository.findAll().stream()
                .map(ServiceDto::mapToDto)
                .collect(Collectors.toList());
    }
}
