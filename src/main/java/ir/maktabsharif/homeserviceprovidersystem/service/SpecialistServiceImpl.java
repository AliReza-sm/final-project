package ir.maktabsharif.homeserviceprovidersystem.service;


import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import ir.maktabsharif.homeserviceprovidersystem.service.Helper.OfferHelperService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Transactional
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long> implements SpecialistService{

    private final SpecialistRepository specialistRepository;
    private final OfferHelperService offerHelperService;
    private final ServiceService serviceService;
    private final TemporaryEmailService temporaryEmailService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public SpecialistServiceImpl(SpecialistRepository specialistRepository,
                                 OfferHelperService offerHelperService,
                                 ServiceService serviceService, TemporaryEmailService temporaryEmailService, VerificationTokenService verificationTokenService, EmailService emailService, PasswordEncoder passwordEncoder) {
        super(specialistRepository);
        this.specialistRepository = specialistRepository;
        this.offerHelperService = offerHelperService;
        this.serviceService = serviceService;
        this.temporaryEmailService = temporaryEmailService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Optional<Specialist> findByEmail(String email) {
        return specialistRepository.findByEmail(email);
    }

    @Override
    public void updateSpecialist(Long specialistId,
                                 SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        if (offerHelperService.existsBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED)) {
            throw new IllegalArgumentException("cannot update specialist with an active job");
        }
        if (specialist.getProfilePhotoBytes() == null){
            specialist.setAccountStatus(AccountStatus.NEW);
        }else {
            specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        }
        setNotNullFilledInSpecialist(specialist, dto);
        specialistRepository.save(specialist);
    }

    private void setNotNullFilledInSpecialist(Specialist specialist,
                                              SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        if (dto.getEmail() != null){
            temporaryEmailService.createTemporaryEmail(specialist.getId(), dto.getEmail());
            VerificationToken verificationToken = verificationTokenService.create(specialist, VerificationTokenType.UPDATE);
            emailService.sendActivationEmail(dto.getEmail(), verificationToken.getToken(), verificationToken.getVerificationTokenType());
        }
        if (dto.getPassword() != null){
            specialist.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }

    @Override
    public void updateSpecialistPhoto(Long specialistId, MultipartFile photo) throws IOException {
        if (photo == null || photo.isEmpty()) {
            throw new NotAllowedException("A photo file must be provided.");
        }
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist with id " + specialistId + " does not exist"));

        if (photo.getSize() > 307200) {
            throw new NotAllowedException("Profile photo size cannot exceed 300KB.");
        }
        specialist.setProfilePhotoBytes(photo.getBytes());
        specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        specialistRepository.save(specialist);
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
        Service service = serviceService.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        specialist.getSpecialistServices().add(service);
        specialistRepository.save(specialist);
    }

    @Override
    public void removeSpecialistFromService(Long specialistId, Long serviceId) {
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("Specialist not found with ID: " + specialistId));
        Service service = serviceService.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + serviceId));

        specialist.getSpecialistServices().remove(service);
        specialistRepository.save(specialist);
    }

    @Override
    public SpecialistDto.SpecialistRating getAverageScore(Long specialistId){
        Specialist specialist = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
        SpecialistDto.SpecialistRating specialistRating = new SpecialistDto.SpecialistRating();
        specialistRating.setAverageScore(specialist.getAverageScore());
        return specialistRating;
    }
}
