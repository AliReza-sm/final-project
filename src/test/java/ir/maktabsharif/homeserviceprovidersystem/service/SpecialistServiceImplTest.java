package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ReviewRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialistServiceImplTest {

    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private SpecialistServiceImpl specialistService;

    private Specialist specialist;
    private Service service;

    @BeforeEach
    void setUp() {
        specialist = new Specialist();
        specialist.setId(1L);
        specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        specialist.setEmail("specialist@gmail.com");
        specialist.setAverageScore(3.25);

        service = new Service();
        service.setId(1L);
        service.setName("fixing");
    }

    @Test
    void register() throws IOException {
        SpecialistDto.SpecialistRequestDto dto = new SpecialistDto.SpecialistRequestDto();
        dto.setEmail("aa@gmail.com");
        when(specialistRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(specialistRepository.save(any(Specialist.class))).thenAnswer(inv -> inv.getArgument(0));
        SpecialistDto.SpecialistResponseDto result = specialistService.register(dto);
        assertNotNull(result);
        assertEquals(AccountStatus.NEW, result.getAccountStatus());
    }

    @Test
    void updateSpecialist() throws IOException {
        SpecialistDto.SpecialistUpdateDto dto = new SpecialistDto.SpecialistUpdateDto();
        dto.setPassword("newPassword");
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        when(offerRepository.existsBySpecialistAndOfferStatus(any(), any())).thenReturn(false);
        specialistService.updateSpecialist(1L, dto);
        verify(specialistRepository, times(1)).save(specialist);
        assertEquals(AccountStatus.NEW, specialist.getAccountStatus());
    }

    @Test
    void findAllSpecialists() {
        when(specialistRepository.findAll()).thenReturn(List.of(specialist));
        List<SpecialistDto.SpecialistResponseDto> result = specialistService.findAllSpecialists();
        assertEquals(1, result.size());
    }

    @Test
    void approveSpecialist() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
//        ArgumentCaptor<Specialist> captor = ArgumentCaptor.forClass(Specialist.class);
        specialistService.approveSpecialist(1L);
        verify(specialistRepository).save(specialist);
        assertEquals(AccountStatus.APPROVED, specialist.getAccountStatus());
    }

    @Test
    void assignSpecialistToService() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        specialistService.assignSpecialistToService(1L, 1L);
        assertTrue(specialist.getSpecialistServices().contains(service));
        verify(specialistRepository, times(1)).save(specialist);
    }

    @Test
    void removeSpecialistFromService() {
        specialist.getSpecialistServices().add(service); // Pre-assign the service
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        specialistService.removeSpecialistFromService(1L, 1L);
        assertFalse(specialist.getSpecialistServices().contains(service));
        verify(specialistRepository, times(1)).save(specialist);
    }

    @Test
    void getOrderHistory() {
        Order order = new Order();
        order.setId(1L);
        order.setService(service);
        Offer offer = new Offer();
        offer.setOrder(order);
        specialist.setAccountStatus(AccountStatus.APPROVED);
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        when(offerRepository.findBySpecialist(specialist)).thenReturn(List.of(offer));
        when(reviewRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        List<SpecialistDto.SpecialistOrderHistoryDto> result = specialistService.getOrderHistory(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderId());
    }

    @Test
    void getAverageScore(){
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        Double averageScore = specialistService.getAverageScore(1L);
        assertNotNull(averageScore);
        assertEquals(3.25, averageScore);
    }

}