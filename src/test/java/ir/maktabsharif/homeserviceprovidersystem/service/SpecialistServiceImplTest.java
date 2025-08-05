package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.OfferHelperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialistServiceImplTest {

    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private ServiceService serviceService;
    @Mock
    private OfferHelperService offerHelperService;

    @Mock
    private TemporaryEmailService temporaryEmailService;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private VerificationTokenServiceImpl verificationTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SpecialistServiceImpl specialistService;

    private Specialist specialist;
    private Service service;
    private VerificationToken verificationToken;

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

        verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken("aa");
        verificationToken.setVerificationTokenType(VerificationTokenType.UPDATE);
    }

    @Test
    void findByEmail() {
        when(specialistRepository.findByEmail(anyString())).thenReturn(Optional.of(specialist));
        assertEquals(specialist, specialistService.findByEmail(specialist.getEmail()).get());
    }

    @Test
    void updateSpecialist() throws IOException {
        SpecialistDto.SpecialistUpdateDto dto = new SpecialistDto.SpecialistUpdateDto();
        dto.setPassword("newPassword");
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        when(offerHelperService.existsBySpecialistAndOfferStatus(any(), any())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        SpecialistDto.SpecialistPhotoUpdateDto specialistPhotoUpdateDto = new SpecialistDto.SpecialistPhotoUpdateDto();
        specialistPhotoUpdateDto.setProfilePhotoData(null);
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
        specialistService.approveSpecialist(1L);
        verify(specialistRepository).save(specialist);
        assertEquals(AccountStatus.APPROVED, specialist.getAccountStatus());
    }

    @Test
    void assignSpecialistToService() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(serviceService.findById(1L)).thenReturn(Optional.of(service));
        specialistService.assignSpecialistToService(1L, 1L);
        assertTrue(specialist.getSpecialistServices().contains(service));
        verify(specialistRepository, times(1)).save(specialist);
    }

    @Test
    void removeSpecialistFromService() {
        specialist.getSpecialistServices().add(service); // Pre-assign the service
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(specialist));
        when(serviceService.findById(1L)).thenReturn(Optional.of(service));
        specialistService.removeSpecialistFromService(1L, 1L);
        assertFalse(specialist.getSpecialistServices().contains(service));
        verify(specialistRepository, times(1)).save(specialist);
    }

    @Test
    void getAverageScore(){
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        SpecialistDto.SpecialistRating specialistRating = specialistService.getAverageScore(1L);
        assertEquals(3.25, specialistRating.getAverageScore());
    }

}