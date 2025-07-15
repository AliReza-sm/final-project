package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    private Specialist specialist;
    private Customer customer;
    private Order order;
    private Offer offer;
    private OfferDto.OfferRequestDto offerRequestDto;

    @BeforeEach
    void setUp() {
        specialist = new Specialist();
        specialist.setId(1L);
        specialist.setEmail("specialist@gmail.com");
        specialist.setAccountStatus(AccountStatus.APPROVED);

        customer = new Customer();
        customer.setId(2L);
        customer.setEmail("customer@gmail.com");

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setProposedPrice(200.0);
        order.setProposedStartDate(LocalDateTime.now().plusDays(1));
        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFER_SELECTION);

        offer = new Offer();
        offer.setId(1L);
        offer.setOrder(order);
        offer.setSpecialist(specialist);

        order.getOffers().add(offer);

        offerRequestDto = new OfferDto.OfferRequestDto();
        offerRequestDto.setOrderId(1L);
        offerRequestDto.setProposedPrice(220.0);
        offerRequestDto.setTimeToEndTheJobInHours(2);
        offerRequestDto.setProposedStartTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void SubmitOffer() {
        when(specialistRepository.findById(anyLong())).thenReturn(Optional.of(specialist));
        when(offerRepository.existsBySpecialistAndOfferStatus(any(), any())).thenReturn(false);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(offerRepository.save(any(Offer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OfferDto.OfferResponseDto result = offerService.submitOffer(offerRequestDto, 1L);
        assertNotNull(result);
        assertEquals(220.0, result.getProposedPrice());
        verify(orderRepository, times(1)).save(order);
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void SelectOffer() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        offerService.selectOffer(1L, 1L, 2L);
        assertEquals(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE, order.getOrderStatus());
        assertEquals(OfferStatus.ACCEPTED, offer.getOfferStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void GetOffersForOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        List<OfferDto.OfferResponseDto> result = offerService.getOffersForOrder(1L, 2L, "price");
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}