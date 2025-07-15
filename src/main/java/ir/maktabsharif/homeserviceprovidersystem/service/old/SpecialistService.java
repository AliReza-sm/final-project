//package ir.maktabsharif.homeserviceprovidersystem.service;
//
//import ir.maktabsharif.homeserviceprovidersystem.dto.*;
//import ir.maktabsharif.homeserviceprovidersystem.entity.*;
//import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
//import ir.maktabsharif.homeserviceprovidersystem.exception.NotAllowedException;
//import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
//import ir.maktabsharif.homeserviceprovidersystem.repository.OfferRepository;
//import ir.maktabsharif.homeserviceprovidersystem.repository.OrderRepository;
//import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
//import ir.maktabsharif.homeserviceprovidersystem.util.MyValidator;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@org.springframework.stereotype.Service
//@RequiredArgsConstructor
//@Transactional
//public class SpecialistService {
//
//    private final SpecialistRepository specialistRepository;
//    private final OrderRepository orderRepository;
//    private final OfferRepository offerRepository;
//
//    @Value("${storage.profile-photo-type}")
//    private String storageProfilePictureType;
//
////    @Value("${storage.file.upload-dir}")
////    private String storageFileUploadDir;
//
//    public SpecialistDto.SpecialistResponseDto register(SpecialistDto.SpecialistRequestDto dto) throws IOException {
//        if (specialistRepository.findByEmail(dto.getEmail()).isPresent()) {
//            throw new AlreadyExistException("specialist with this email already exist");
//        }
//        Specialist specialist = new Specialist();
//        specialist.setFirstname(dto.getFirstName());
//        specialist.setLastname(dto.getLastName());
//        specialist.setEmail(dto.getEmail());
//        specialist.setPassword(dto.getPassword());
//        specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
//        specialist.setSpecialistStatus(SpecialistStatus.FREE);
//        if (dto.getProfilePhotoData() != null){
//            specialist.setProfilePhotoBytes(dto.getProfilePhotoData().getBytes());
//        }
//        Wallet wallet = new Wallet();
//        specialist.setWallet(wallet);
//        Specialist savedSpecialist = specialistRepository.save(specialist);
//        wallet.setUser(savedSpecialist);
//        return SpecialistDto.mapToDto(savedSpecialist);
//    }
//
//    public void updateSpecialist(SpecialistDto.SpecialistUpdateDto dto) throws IOException {
//        Specialist specialist = specialistRepository.findById(dto.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
//        if (offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED).isPresent()) {
//            throw new IllegalArgumentException("cannot update specialist with an active job");
//        }
//        setNotNullFilledInSpecialist(specialist, dto);
//        specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
//        specialistRepository.update(specialist);
//    }
//
//    private void setNotNullFilledInSpecialist(Specialist specialist, SpecialistDto.SpecialistUpdateDto dto) throws IOException {
//        if (dto.getEmail() != null){
//            if (specialistRepository.findByEmail(dto.getEmail()).isPresent()) {
//                throw new AlreadyExistException("email already exist");
//            }
//            specialist.setEmail(dto.getEmail());
//        }
//        if (dto.getPassword() != null){
//            specialist.setPassword(dto.getPassword());
//        }
//        if (dto.getProfilePhotoData() != null){
//            specialist.setProfilePhotoBytes(dto.getProfilePhotoData().getBytes());
//        }
//    }
//
//    public List<OrderDto.OrderResponseDto> viewAvailableOrders(Long specialistId) {
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
//        List<OrderDto.OrderResponseDto> orders = new ArrayList<>();
//        orders.addAll(findOrderByStatusAndService(specialist, OrderStatus.WAITING_FOR_SPECIALIST_OFFERS));
//        orders.addAll(findOrderByStatusAndService(specialist, OrderStatus.WAITING_FOR_OFFER_SELECTION));
//        return orders;
//    }
//
//    private List<OrderDto.OrderResponseDto> findOrderByStatusAndService(Specialist specialist, OrderStatus orderStatus) {
//        return orderRepository.findByStatusAndService(orderStatus, specialist.getSpecialistServices())
//                .stream().map(OrderDto::mapToDto).toList();
//    }
//
//    public OfferDto.OfferResponseDto submitOffer(Long specialistId, OfferDto.OfferRequestDto dto) {
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
//        if (specialist.getAccountStatus() != AccountStatus.APPROVED) {
//            throw new NotAllowedException("specialist account is not approved");
//        }
//        if (offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.ACCEPTED).isPresent()
//            || offerRepository.findBySpecialistAndOfferStatus(specialist, OfferStatus.PENDING).isPresent()) {
//            throw new NotAllowedException("cannot submit offer while having an active job");
//        }
//        Order order = orderRepository.findById(dto.getOrderId())
//                .orElseThrow(() -> new ResourceNotFoundException("order with this id does not exist"));
//        if (order.getProposedPrice() < dto.getProposedPrice()){
//            throw new NotAllowedException("cannot submit offer with proposed price less than order proposed price");
//        }
//        if (dto.getProposedStartTime().isBefore(order.getProposedStartDate())){
//            throw new NotAllowedException("offer proposed start time can not be before order proposed start time");
//        }
//        Offer offer = new Offer();
//        offer.setOfferStatus(OfferStatus.PENDING);
//        offer.setSpecialist(specialist);
//        offer.setOrder(order);
//        offer.setProposedPrice(dto.getProposedPrice());
//        offer.setProposedStartTime(dto.getProposedStartTime());
//        offer.setTimeToEndTheJobInHours(dto.getTimeToEndTheJobInHours());
//        if (order.getOffers().size() == 1){
//            order.setOrderStatus(OrderStatus.WAITING_FOR_OFFER_SELECTION);
//        }
//        return OfferDto.mapToDto(offerRepository.save(offer));
//    }
//
//    public Double showWalletBalance(Long specialistId) {
//        Specialist specialist = specialistRepository.findById(specialistId)
//                .orElseThrow(() -> new ResourceNotFoundException("specialist with this id does not exist"));
//        return specialist.getWallet().getBalance();
//    }
//}
