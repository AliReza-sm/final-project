//package ir.maktabsharif.homeserviceprovidersystem.service;
//
//import ir.maktabsharif.homeserviceprovidersystem.dto.ManagerUpdateDto;
//import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
//import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
//import ir.maktabsharif.homeserviceprovidersystem.entity.*;
//import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
//import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
//import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
//import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
//import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
//import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
//import ir.maktabsharif.homeserviceprovidersystem.util.MyValidator;
//import lombok.RequiredArgsConstructor;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@org.springframework.stereotype.Service
//@RequiredArgsConstructor
//@Transactional
//public class ManagerService {
//
//    private final ServiceRepository serviceRepository;
//    private final OrderRepository orderRepository;
//    private final SpecialistRepository specialistRepository;
//    private final ManagerRepository managerRepository;
//
//    public ServiceDto.ServiceResponseDto createService(ServiceDto.ServiceRequestDto serviceRequestDto) {
//        if (serviceRepository.findByName(serviceRequestDto.getName()).isPresent()){
//            throw new AlreadyExistException("service with name " + serviceRequestDto.getName() + " already exist");
//        }
//        Service service = ServiceDto.mapToEntity(serviceRequestDto);
//        if (serviceRequestDto.getParentServiceId() != null){
//           Service parent = serviceRepository.findById(serviceRequestDto.getParentServiceId())
//                    .orElseThrow(() -> new ResourceNotFoundException("parent service with id " + serviceRequestDto.getParentServiceId() + " not found"));
//           service.setParentService(parent);
//           parent.getSubServices().add(service);
//        }
//        return ServiceDto.mapToDto(serviceRepository.save(service));
//    }
//
//    public ServiceDto.ServiceResponseDto updateService(ServiceDto.ServiceUpdateDto serviceUpdateDto) {
//        Service service = serviceRepository.findById(serviceUpdateDto.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("service with id " + serviceUpdateDto.getId() + " not found"));
//        setNotNullFilledInService(service, serviceUpdateDto);
//        Service updatedService = serviceRepository.update(service);
//        return ServiceDto.mapToDto(updatedService);
//    }
//
//    private void setNotNullFilledInService(Service service, ServiceDto.ServiceUpdateDto serviceUpdateDto) {
//        if (serviceUpdateDto.getName() != null && serviceRepository.findByName(serviceUpdateDto.getName()).isPresent()){
//            throw new AlreadyExistException("service with name " + serviceUpdateDto.getName() + " already exist");
//        }
//        if (serviceUpdateDto.getName() != null){service.setName(serviceUpdateDto.getName());}
//        if (serviceUpdateDto.getDescription() != null){
//            service.setDescription(serviceUpdateDto.getDescription());
//        }
//        if (serviceUpdateDto.getBasePrice() != null){
//            service.setBasePrice(serviceUpdateDto.getBasePrice());
//        }
//        if (serviceUpdateDto.getParentServiceId() != null){
//            Service parent = serviceRepository.findById(serviceUpdateDto.getParentServiceId())
//                    .orElseThrow(() -> new ResourceNotFoundException("parent service with id " + serviceUpdateDto.getParentServiceId() + " not found"));
//            service.getParentService().getSubServices().remove(service);
//            service.setParentService(parent);
//            parent.getSubServices().add(service);
//        }
//    }
//
//    public void deleteService(Long serviceId) {
//        Service service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new ResourceNotFoundException("service with id " + serviceId + " not found"));
//        if (orderRepository.existsByServiceId(serviceId)) {
//            throw new IllegalStateException("Cannot delete a service that is currently associated with one or more orders.");
//        }
//        if (service.getParentService() != null){
//            service.getParentService().getSubServices().remove(service);
//        }
//        serviceRepository.deleteById(serviceId);
//    }
//
//    public void approveSpecialist(Long specialistId){
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist with id " + specialistId + " not found"));
//        specialist.setAccountStatus(AccountStatus.APPROVED);
//        specialistRepository.save(specialist);
//    }
//
//    public void assignSpecialistToService(Long serviceId, Long specialistId) {
//        Service service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist not found"));
//        specialist.getSpecialistServices().add(service);
//        specialistRepository.save(specialist);
//    }
//
//    public void removeSpecialistFromService(Long serviceId, Long specialistId) {
//        Service service = serviceRepository.findById(serviceId)
//                .orElseThrow(() -> new ResourceNotFoundException("service not found"));
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist not found"));
//        specialist.getSpecialistServices().remove(service);
//        specialistRepository.save(specialist);
//    }
//
//    public List<SpecialistDto.SpecialistResponseDto> findAllSpecialists() {
//        return specialistRepository.findAll().stream()
//                .map(SpecialistDto::mapToDto)
//                .toList();
//    }
//
//    public void updateManagerCredentials(ManagerUpdateDto managerUpdateDto){
//        Manager manager = managerRepository.findById(1L)
//                .orElseThrow(() -> new ResourceNotFoundException("manager not found"));
//        manager.setEmail(managerUpdateDto.email());
//        manager.setPassword(managerUpdateDto.password());
//        managerRepository.update(manager);
//    }
//}
