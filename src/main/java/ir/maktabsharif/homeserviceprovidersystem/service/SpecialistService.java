package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.*;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import ir.maktabsharif.homeserviceprovidersystem.util.MyValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    @Value("${storage.profile-photo-type}")
    private String storageProfilePictureType;

//    @Value("${storage.file.upload-dir}")
//    private String storageFileUploadDir;

    public SpecialistResponseDto register(SpecialistRegistrationDto dto) throws IOException {
        MyValidator.validate(dto);
        if (specialistRepository.findByEmail(dto.email()).isPresent()) {
            throw new AlreadyExistException("specialist with this email already exist");
        }
        Specialist specialist = new Specialist();
        specialist.setFirstname(dto.firstname());
        specialist.setLastname(dto.lastname());
        specialist.setEmail(dto.email());
        specialist.setPassword(dto.password());
        specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        specialist.setSpecialistStatus(SpecialistStatus.FREE);
        specialist.setProfilePhotoBytes(dto.profilePhotoData().getBytes());
        Wallet wallet = new Wallet();
        specialist.setWallet(wallet);
        Specialist savedSpecialist = specialistRepository.save(specialist);
        wallet.setUser(savedSpecialist);
        return modelMapper.map(savedSpecialist, SpecialistResponseDto.class);
    }

    public void updateSpecialist(Long specialistId, UserUpdateDto dto) {
        MyValidator.validate(dto);
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        if (offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED).isPresent()) {
            throw new IllegalArgumentException("cannot update specialist with an active job");
        }
        specialist.setFirstname(dto.firstName());
        specialistRepository.update(specialist);
    }

    public List<OrderResponseDto> viewAvailableOrders(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        return orderRepository.findByStatusAndService(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS, specialist.getSpecialistServices())
                .stream().map(order -> modelMapper.map(order, OrderResponseDto.class))
                .toList();
    }

    public OfferResponseDto submitOffer(Long specialistId, OfferRequestDto dto) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        if (specialist.getAccountStatus() != AccountStatus.APPROVED) {
            throw new NotAllowedException("specialist account is not approved");
        }
        if (offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED).isPresent()
            || offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.PENDING).isPresent()) {
            throw new NotAllowedException("cannot submit offer while having an active job");
        }
        Order order = orderRepository.findById(dto.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("order with this id does not exist"));
        Offer offer = new Offer();
        offer.setOfferStatus(OfferStatus.PENDING);
        offer.setSpecialist(specialist);
        offer.setOrder(order);
        offer.setProposedPrice(dto.proposedPrice());
        offer.setProposedStartTime(dto.proposedStartTime());
        offer.setTimeToEndTheJobInHours(dto.durationInHours());
        offerRepository.save(offer);
        return modelMapper.map(offer, OfferResponseDto.class);
    }

    public void changeJobToCompleted(Long specialistId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order with this id does not exist"));
        if (order.getSelectedOffer() == null || !order.getSelectedOffer().getSpecialist().getId().equals(specialistId)) {
            throw new NotAllowedException("cannot change job to completed because specialist is not assigned to this order");
        }
        if (order.getOrderStatus() != OrderStatus.IN_PROGRESS){
            throw new NotAllowedException("order must be in progress");
        }
        order.setOrderStatus(OrderStatus.DONE);
        orderRepository.update(order);
    }

    public Double showWalletBalance(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        return specialist.getWallet().getBalance();
    }
}
