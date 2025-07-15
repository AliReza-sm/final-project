package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private Service service;
    private ServiceDto.ServiceRequestDto serviceRequestDto;

    @BeforeEach
    void setUp() {
        service = new Service();
        service.setId(1L);
        service.setName("fixing");

        serviceRequestDto = new ServiceDto.ServiceRequestDto();
        serviceRequestDto.setName("fixing");
        serviceRequestDto.setBasePrice(50.0);
    }

    @Test
    void createService() {
        when(serviceRepository.findByName("fixing")).thenReturn(Optional.empty());
        when(serviceRepository.save(any(Service.class))).thenReturn(service);
        ServiceDto.ServiceResponseDto result = serviceService.createService(serviceRequestDto);
        assertNotNull(result);
        assertEquals("fixing", result.getName());
    }

    @Test
    void updateService() {
        ServiceDto.ServiceRequestDto updateDto = new ServiceDto.ServiceRequestDto();
        updateDto.setDescription("fix everything");
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceRepository.save(any(Service.class))).thenReturn(service);
        serviceService.updateService(1L, updateDto);
        assertEquals("fix everything", service.getDescription());
        verify(serviceRepository, times(1)).save(service);
    }

    @Test
    void deleteService() {
        when(serviceRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.existsByServiceId(1L)).thenReturn(false);
        serviceService.deleteService(1L);
        verify(serviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void findById() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        ServiceDto.ServiceResponseDto result = serviceService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findAll() {
        when(serviceRepository.findAll()).thenReturn(List.of(service));
        List<ServiceDto.ServiceResponseDto> result = serviceService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

}