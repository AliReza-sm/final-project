package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor

public class OfferServiceImpl implements OfferService{

    private final OfferRepository offerRepository;
    private final SpecialistRepository specialistRepository;
    private final OrderRepository orderRepository;


    @Override
    public OfferDto.OfferResponseDto submitOffer(OfferDto.OfferRequestDto requestDto, Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with id: " + specialistId));

        if (specialist.getAccountStatus() != AccountStatus.APPROVED) {
            throw new NotAllowedException("Your account is not approved to submit offers.");
        }

        if (offerRepository.existsBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED)) {
            throw new NotAllowedException("Cannot submit a new offer while you have an active job.");
        }

        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + requestDto.getOrderId()));

        if (requestDto.getProposedPrice() < order.getProposedPrice()) {
            throw new NotAllowedException("Offer price cannot be less than the customers proposed price.");
        }
        if (requestDto.getProposedStartTime().isBefore(order.getProposedStartDate())) {
            throw new NotAllowedException("Offer start time cannot be before the customers proposed start time.");
        }

        Offer offer = OfferDto.mapToEntity(requestDto);
        offer.setSpecialist(specialist);
        offer.setOrder(order);
        offer.setOfferStatus(OfferStatus.PENDING);

        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFER_SELECTION);
        orderRepository.save(order);

        Offer savedOffer = offerRepository.save(offer);
        return OfferDto.mapToDto(savedOffer);
    }

    @Override
    public void selectOffer(Long orderId, Long offerId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You are not the owner of this order.");
        }
        if (order.getOrderStatus() != OrderStatus.WAITING_FOR_OFFER_SELECTION) {
            throw new NotAllowedException("Order is not in the WAITING_FOR_OFFER_SELECTION state.");
        }

        Offer selectedOffer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with ID: " + offerId));

        if (!selectedOffer.getOrder().getId().equals(orderId)) {
            throw new NotAllowedException("This offer does not belong to the specified order.");
        }

        order.setSelectedOffer(selectedOffer);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
        order.getOffers().forEach(offer -> {
            if (offer.getId().equals(offerId)) {
                offer.setOfferStatus(OfferStatus.ACCEPTED);
            } else {
                offer.setOfferStatus(OfferStatus.REJECTED);
            }
        });
        orderRepository.save(order);
    }

    @Override
    public List<OfferDto.OfferResponseDto> getOffersForOrder(Long orderId, Long customerId, String sortBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new NotAllowedException("You are not allowed to view offers for this order.");
        }

        Comparator<Offer> comparator;
        if ("rating".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparing(offer -> offer.getSpecialist().getAverageScore(), Comparator.reverseOrder());
        } else {
            comparator = Comparator.comparing(Offer::getProposedPrice);
        }
        return order.getOffers().stream()
                .sorted(comparator)
                .map(OfferDto::mapToDto)
                .collect(Collectors.toList());
    }
}
