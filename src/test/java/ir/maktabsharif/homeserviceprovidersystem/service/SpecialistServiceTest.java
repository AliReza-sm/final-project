package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.*;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialistServiceTest {

    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OfferRepository offerRepository;


    @InjectMocks
    private SpecialistService specialistService;
    private Specialist testSpecialist;
    private Order testOrder;
    private Service testService;

    @BeforeEach
    void setUp() {
        testService = new Service();

        testSpecialist = new Specialist();
        testSpecialist.setFirstname("a");
        testSpecialist.setAccountStatus(AccountStatus.APPROVED);
        testSpecialist.setSpecialistServices(Set.of(testService));
        testSpecialist.setWallet(new Wallet());
        testSpecialist.getWallet().setBalance(100D);

        Customer testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setWallet(new Wallet());
        testCustomer.getWallet().setBalance(100D);

        testOrder = new Order();
        testOrder.setService(testService);
        testOrder.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
        testOrder.setCustomer(testCustomer);
    }

    @Test
    void register_ShouldSaveSpecialist() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        SpecialistDto.SpecialistRequestDto dto = new SpecialistDto.SpecialistRequestDto();
        dto.setFirstName("a");
        dto.setLastName("a");
        dto.setEmail("a@mail.com");
        dto.setPassword("AaBbCc11");
        dto.setProfilePhotoData(file);
        when(specialistRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        specialistService.register(dto);
        verify(specialistRepository, times(1)).save(any(Specialist.class));
    }

    @Test
    void updateSpecialist() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        SpecialistDto.SpecialistUpdateDto dto = new SpecialistDto.SpecialistUpdateDto();
        dto.setId(1L);
        dto.setEmail("a@mail.com");
        dto.setPassword("AaBbCc11");
        dto.setProfilePhotoData(file);
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        when(offerRepository.findBySpecialistAndOfferStatus(testSpecialist, OfferStatus.ACCEPTED)).thenReturn(Optional.empty());
        specialistService.updateSpecialist(dto);
        verify(specialistRepository, times(1)).update(testSpecialist);
        assertEquals("a", testSpecialist.getFirstname());
    }

    @Test
    void viewAvailableOrders() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        //when(CustomerDto.mapToDto(Mockito.mock(Customer.class))).thenReturn(Mockito.mock(CustomerDto.CustomerResponseDto.class));
        when(orderRepository.findByStatusAndService(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS, Set.of(testService)))
                .thenReturn(Collections.singletonList(testOrder));
        List<OrderDto.OrderResponseDto> result = specialistService.viewAvailableOrders(1L);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void submitOffer() {
        OfferDto.OfferRequestDto dto = new OfferDto.OfferRequestDto();
        dto.setOrderId(1L);
        dto.setProposedPrice(200D);
        dto.setTimeToEndTheJobInHours(2);
        dto.setProposedStartTime(LocalDateTime.now().plusDays(1));
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        when(offerRepository.findBySpecialistAndOfferStatus(testSpecialist, OfferStatus.ACCEPTED)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        specialistService.submitOffer(1L, dto);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void showWalletBalance() {
        when(specialistRepository.findById(1L)).thenReturn(Optional.of(testSpecialist));
        Double balance = specialistService.showWalletBalance(1L);
        assertEquals(100D, balance);
    }
}