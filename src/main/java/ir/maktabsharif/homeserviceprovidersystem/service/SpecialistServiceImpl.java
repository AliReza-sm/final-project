package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor

public class SpecialistServiceImpl implements SpecialistService{

    private final SpecialistRepository specialistRepository;
    private final OfferRepository offerRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public SpecialistDto.SpecialistResponseDto register(SpecialistDto.SpecialistRequestDto dto) throws IOException {
        if (specialistRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new AlreadyExistException("specialist with this email already exist");
        }
        Specialist specialist = new Specialist();
        specialist.setFirstname(dto.getFirstName());
        specialist.setLastname(dto.getLastName());
        specialist.setEmail(dto.getEmail());
        specialist.setPassword(dto.getPassword());
        specialist.setRole(Role.SPECIALIST);
        specialist.setSpecialistStatus(SpecialistStatus.ACTIVE);
        if (dto.getProfilePhotoData() != null){
            specialist.setProfilePhotoBytes(dto.getProfilePhotoData().getBytes());
            specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        } else {
            specialist.setAccountStatus(AccountStatus.NEW);
        }
        Wallet wallet = new Wallet();
        specialist.setWallet(wallet);
        Specialist savedSpecialist = specialistRepository.save(specialist);
        wallet.setUser(savedSpecialist);
        return SpecialistDto.mapToDto(savedSpecialist);
    }

    @Override
    public void updateSpecialist(Long specialistId, SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        if (offerRepository.existsBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED)) {
            throw new IllegalArgumentException("cannot update specialist with an active job");
        }
        if (dto.getProfilePhotoData() == null && specialist.getProfilePhotoBytes() == null){
            specialist.setAccountStatus(AccountStatus.NEW);
        }else {
            specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        }
        setNotNullFilledInSpecialist(specialist, dto);
        specialistRepository.save(specialist);
    }

    private void setNotNullFilledInSpecialist(Specialist specialist, SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        if (dto.getEmail() != null){
            if (specialistRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new AlreadyExistException("email already exist");
            }
            specialist.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null){
            specialist.setPassword(dto.getPassword());
        }
        if (dto.getProfilePhotoData() != null){
            specialist.setProfilePhotoBytes(dto.getProfilePhotoData().getBytes());
        }
    }

    @Override
    public List<SpecialistDto.SpecialistResponseDto> findAllSpecialists() {
        return specialistRepository.findAll().stream()
                .map(SpecialistDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void approveSpecialist(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with ID: " + specialistId));
        if (specialist.getAccountStatus() == AccountStatus.NEW) {
            throw new NotAllowedException("Specialist profile photo hasn't been uploaded yet");
        }
        specialist.setAccountStatus(AccountStatus.APPROVED);
        specialistRepository.save(specialist);
    }

    @Override
    public void assignSpecialistToService(Long specialistId, Long serviceId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with ID: " + specialistId));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        specialist.getSpecialistServices().add(service);
        specialistRepository.save(specialist);
    }

    @Override
    public void removeSpecialistFromService(Long specialistId, Long serviceId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with ID: " + specialistId));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        specialist.getSpecialistServices().remove(service);
        specialistRepository.save(specialist);
    }

    @Override
    public List<SpecialistDto.SpecialistOrderHistoryDto> getOrderHistory(Long specialistId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        if (specialist.getAccountStatus() != AccountStatus.APPROVED) {
            throw new NotAllowedException("cannot get order history");
        }
        List<Offer> offers = offerRepository.findBySpecialist(specialist);
        return offers.stream().map(offer -> {
            Order order = offer.getOrder();
            SpecialistDto.SpecialistOrderHistoryDto orderHistoryDto = new SpecialistDto.SpecialistOrderHistoryDto();
            orderHistoryDto.setOrderId(order.getId());
            orderHistoryDto.setServiceName(order.getService().getName());
            orderHistoryDto.setOrderStatus(order.getOrderStatus());
            orderHistoryDto.setOrderCreatedDate(order.getOrderCreatedDate());
            orderHistoryDto.setYourProposedPrice(offer.getProposedPrice());
            orderHistoryDto.setYourRatingForOrder(reviewRepository.findByOrderId(order.getId())
                    .map(Review::getRating)
                    .orElse(null));
            return orderHistoryDto;
        }).toList();
    }

    @Override
    public Double getAverageScore(Long specialistId){
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        return specialist.getAverageScore();
    }
}
