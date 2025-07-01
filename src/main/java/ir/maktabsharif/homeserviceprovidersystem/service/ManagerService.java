package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ManagerUpdateDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceRequestDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceResponseDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistResponseDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {

    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final SpecialistRepository specialistRepository;
    private final ManagerRepository managerRepository;

    public ServiceResponseDto createService(ServiceRequestDto serviceRequestDto) {
        if (serviceRepository.findByName(serviceRequestDto.name()).isPresent()){
            throw new AlreadyExistException("service with name " + serviceRequestDto.name() + " already exist");
        }
        Service service = modelMapper.map(serviceRequestDto, Service.class);
        if (serviceRequestDto.parentServiceId() != null){
           Service parent = serviceRepository.findById(serviceRequestDto.parentServiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("parent service with id " + serviceRequestDto.parentServiceId() + " not found"));
           service.setParentService(parent);
        }
        Service savedService = serviceRepository.save(service);
        return modelMapper.map(savedService, ServiceResponseDto.class);
    }

    public ServiceResponseDto updateService(Long serviceId, ServiceRequestDto serviceRequestDto) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("service with id " + serviceId + " not found"));
        modelMapper.map(serviceRequestDto, service);
        Service updatedService = serviceRepository.update(service);
        return modelMapper.map(updatedService, ServiceResponseDto.class);
    }

    public void deleteService(Long serviceId) {
        if (!serviceRepository.findById(serviceId).isPresent()){
            throw new ResourceNotFoundException("service with id " + serviceId + " not found");
        }
        List<Order> allOrder = orderRepository.findAll();
        for (Order order : allOrder){
            if (order.getService().getId().equals(serviceId) && order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)){
                throw new RuntimeException("order with this service id in Order is in progress");
            }
        }
        serviceRepository.deleteById(serviceId);
    }

    public void assignSpecialistToService(Long serviceId, Long specialistId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist not found"));
        specialist.getSpecialistServices().add(service);
        specialistRepository.save(specialist);
    }

    public void removeSpecialistFromService(Long serviceId, Long specialistId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist not found"));
        specialist.getSpecialistServices().remove(service);
        specialistRepository.save(specialist);
    }

    public List<SpecialistResponseDto> findAllSpecialists() {
        return specialistRepository.findAll().stream()
                .map(s -> modelMapper.map(s, SpecialistResponseDto.class))
                .toList();
    }

    public void updateManagerCredentials(ManagerUpdateDto managerUpdateDto){
        Manager manager = managerRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("manager not found"));
        manager.setEmail(managerUpdateDto.email());
        manager.setPassword(managerUpdateDto.password());
        managerRepository.update(manager);
    }
}
