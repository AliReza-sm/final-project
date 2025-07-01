package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ManagerUpdateDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceRequestDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistResponseDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ManagerService managerService;
    private Specialist testSpecialist;
    private Service testService;
    private Manager testManager;

    @BeforeEach
    void setUp() {
        testSpecialist = new Specialist();
        testSpecialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        testService = new Service();
        testService.setName("computer");
        testManager = new Manager();
        testManager.setEmail("manager@mail.com");
    }

    @Test
    void createService_ShouldSaveNewService() {
        ServiceRequestDto dto = new ServiceRequestDto("new service", "", 100D, null);
        when(serviceRepository.findByName(dto.name())).thenReturn(Optional.empty());
        when(modelMapper.map(dto, Service.class)).thenReturn(new Service());
        managerService.createService(dto);
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    void createService_WhenNameExists_ShouldThrowException(){
        ServiceRequestDto dto = new ServiceRequestDto("new service", "", 100D, null);
        when(serviceRepository.findByName(dto.name())).thenReturn(Optional.of(new Service()));
        assertThrows(AlreadyExistException.class, () -> managerService.createService(dto));
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void updateService_ShouldSaveChanges() {
        ServiceRequestDto dto = new ServiceRequestDto("aa", "bb", 100D, 1L);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        managerService.updateService(1L, dto);
        verify(serviceRepository, times(1)).update(testService);
    }

    @Test
    void deleteService_ShouldDeleteService() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(testService));
        managerService.deleteService(1L);
        verify(serviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void approveSpecialist() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        managerService.approveSpecialist(1L);
        ArgumentCaptor<Specialist> specialistCaptor = ArgumentCaptor.forClass(Specialist.class);
        verify(specialistRepository).save(specialistCaptor.capture());
        assertEquals(AccountStatus.APPROVED, specialistCaptor.getValue().getAccountStatus());
    }

    @Test
    void assignSpecialistToService(){
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        when(serviceRepository.findById(10L)).thenReturn(Optional.of(testService));
        managerService.assignSpecialistToService(10L, 1L);
        assertTrue(testSpecialist.getSpecialistServices().contains(testService));
        verify(specialistRepository, times(1)).save(testSpecialist);
    }

    @Test
    void removeSpecialistFromService(){
        testSpecialist.getSpecialistServices().add(testService);
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        when(serviceRepository.findById(10L)).thenReturn(Optional.of(testService));
        managerService.removeSpecialistFromService(10L, 1L);
        assertFalse(testSpecialist.getSpecialistServices().contains(testService));
        verify(specialistRepository, times(1)).save(testSpecialist);
    }

    @Test
    void findAllSpecialists_ShouldReturnAllSpecialists() {
        when(specialistRepository.findAll()).thenReturn(Collections.singletonList(testSpecialist));
        List<SpecialistResponseDto> result = managerService.findAllSpecialists();
        assertEquals(1, result.size());
        verify(modelMapper, times(1)).map(testSpecialist, SpecialistResponseDto.class);
    }

    @Test
    void updateManagerCredentials(){
        ManagerUpdateDto dto = new ManagerUpdateDto("admin@mail.com", "password");
        when(managerRepository.findById(1L)).thenReturn(Optional.of(testManager));
        managerService.updateManagerCredentials(dto);
        ArgumentCaptor<Manager> managerCaptor = ArgumentCaptor.forClass(Manager.class);
        verify(managerRepository).update(managerCaptor.capture());
        Manager savedManager = managerCaptor.getValue();
        assertEquals("admin@mail.com", savedManager.getEmail());
        assertEquals("password", savedManager.getPassword());
    }
}