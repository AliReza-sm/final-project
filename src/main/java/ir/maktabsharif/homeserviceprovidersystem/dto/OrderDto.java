package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    @Data
    public static class OrderResponseDto {
        private Long id;
        private OrderStatus status;
        private Double proposedPrice;
        private String description;
        private String address;
        private LocalDateTime proposedStartDate;
        private ServiceDto.ServiceResponseDto service;
        private CustomerDto.CustomerResponseDto customer;
        private List<OfferDto.OfferResponseDto> offers;
        private OfferDto.OfferResponseDto selectedOffer;
    }

    @Data
    public static class OrderRequestDto {
        @NotNull(message = "Service ID cannot be null")
        private Long serviceId;
        @NotNull(message = "Proposed price cannot be null")
        @Positive(message = "Proposed price must be positive")
        private Double proposedPrice;
        private String description;
        @NotBlank(message = "Address cannot be blank")
        private String address;
        @NotNull(message = "Proposed start date cannot be null")
        @Future(message = "Proposed start date must be in the future")
        private LocalDateTime proposedStartDate;
    }


    @Data
    public static class CustomerOrderHistoryDto {
        private Long orderId;
        private String serviceName;
        private OrderStatus status;
        private LocalDateTime createdDate;
        private Double finalPrice;
        private OfferDto.OfferResponseDto selectedOffer;
    }

    @Data
    public static class ManagerOrderHistorySummaryDto {
        private Long orderId;
        private String serviceName;
        private String customerFullName;
        private String specialistFullName;
        private OrderStatus status;
        private LocalDateTime createdDate;
    }

    @Data
    public static class ManagerOrderDetailDto {
        private Long id;
        private OrderStatus status;
        private Double proposedPrice;
        private String description;
        private String address;
        private LocalDateTime proposedStartDate;
        private ServiceDto.ServiceResponseDto service;
        private CustomerDto.CustomerResponseDto customer;
        private SpecialistDto.SpecialistResponseDto specialist;
        private List<OfferDto.OfferResponseDto> offers;
        private ReviewDto.ReviewResponseDto review;
    }

    @Data
    public static class SpecialistOrderHistoryDto {
        private Long orderId;
        private String serviceName;
        private OrderStatus orderStatus;
        private LocalDateTime orderCreatedDate;
        private Double yourProposedPrice;
        private Integer yourRatingForOrder;
    }

    @Data
    public static class SpecialistOrderDetailDto {
        private Long id;
        private OrderStatus status;
        private Double proposedPrice;
        private String description;
        private String address;
        private LocalDateTime proposedStartDate;
        private ServiceDto.ServiceResponseDto service;
        private OfferDto.OfferResponseDto yourOffer;
        private CustomerDto.CustomerResponseDto customer;
        private Integer rating;
    }

    public static OrderResponseDto mapToDto(Order order) {
        if (order == null) return null;
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setStatus(order.getOrderStatus());
        dto.setProposedPrice(order.getProposedPrice());
        dto.setDescription(order.getDescription());
        dto.setAddress(order.getAddress());
        dto.setProposedStartDate(order.getProposedStartDate());
        dto.setService(ServiceDto.mapToDto(order.getService()));
        dto.setCustomer(CustomerDto.mapToDto(order.getCustomer()));
        if (order.getOffers() != null) {
            dto.setOffers(order.getOffers().stream().map(OfferDto::mapToDto).collect(Collectors.toList()));
        } else {
            dto.setOffers(Collections.emptyList());
        }
        dto.setSelectedOffer(OfferDto.mapToDto(order.getSelectedOffer()));
        return dto;
    }

    public static Order mapToEntity(OrderRequestDto dto) {
        Order order = new Order();
        order.setProposedPrice(dto.getProposedPrice());
        order.setDescription(dto.getDescription());
        order.setAddress(dto.getAddress());
        order.setProposedStartDate(dto.getProposedStartDate());
        return order;
    }

    public static CustomerOrderHistoryDto mapToCustomerOrderHistoryDto(Order order) {
        CustomerOrderHistoryDto dto = new CustomerOrderHistoryDto();
        dto.setOrderId(order.getId());
        dto.setServiceName(order.getService().getName());
        dto.setStatus(order.getOrderStatus());
        dto.setCreatedDate(order.getOrderCreatedDate());
        Offer selectedOffer = order.getSelectedOffer();
        if (selectedOffer != null) {
            dto.setFinalPrice(selectedOffer.getProposedPrice());
            dto.setSelectedOffer(OfferDto.mapToDto(selectedOffer));
        } else {
            dto.setFinalPrice(order.getProposedPrice());
        }
        return dto;
    }

    public static ManagerOrderHistorySummaryDto mapToManagerOrderHistorySummaryDto(Order order) {
        ManagerOrderHistorySummaryDto dto = new ManagerOrderHistorySummaryDto();
        dto.setOrderId(order.getId());
        dto.setServiceName(order.getService().getName());
        dto.setCustomerFullName(order.getCustomer().getFirstname() + " " + order.getCustomer().getLastname());
        if (order.getSelectedOffer() != null && order.getSelectedOffer().getSpecialist() != null) {
            Specialist specialist = order.getSelectedOffer().getSpecialist();
            dto.setSpecialistFullName(specialist.getFirstname() + " " + specialist.getLastname());
        } else {
            dto.setSpecialistFullName("null");
        }
        dto.setStatus(order.getOrderStatus());
        dto.setCreatedDate(order.getOrderCreatedDate());
        return dto;
    }

    public static ManagerOrderDetailDto mapToManagerOrderDetailDto(Order order, Review review) {
        ManagerOrderDetailDto dto = new ManagerOrderDetailDto();
        dto.setId(order.getId());
        dto.setStatus(order.getOrderStatus());
        dto.setProposedPrice(order.getProposedPrice());
        dto.setDescription(order.getDescription());
        dto.setAddress(order.getAddress());
        dto.setProposedStartDate(order.getProposedStartDate());
        dto.setService(ServiceDto.mapToDto(order.getService()));
        dto.setCustomer(CustomerDto.mapToDto(order.getCustomer()));
        if (order.getSelectedOffer() != null && order.getSelectedOffer().getSpecialist() != null) {
            Specialist specialist = order.getSelectedOffer().getSpecialist();
            dto.setSpecialist(SpecialistDto.mapToDto(specialist));
        }
        if (order.getOffers() != null) {
            dto.setOffers(order.getOffers().stream().map(OfferDto::mapToDto).collect(Collectors.toList()));
        }
        if (review != null) {
            dto.setReview(ReviewDto.mapToDto(review));
        }else{
            dto.setReview(null);
        }

        return dto;
    }

    public static SpecialistOrderHistoryDto mapToSpecialistOrderHistoryDto (Offer offer) {
        Order order = offer.getOrder();
        SpecialistOrderHistoryDto dto = new SpecialistOrderHistoryDto();
        dto.setOrderId(order.getId());
        dto.setServiceName(order.getService().getName());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setOrderCreatedDate(order.getOrderCreatedDate());
        dto.setYourProposedPrice(offer.getProposedPrice());
        return dto;
    }

    public static SpecialistOrderDetailDto mapToSpecialistOrderDetailDto(Order order, Offer specialistOffer, Integer rating) {
        SpecialistOrderDetailDto dto = new SpecialistOrderDetailDto();
        dto.setId(order.getId());
        dto.setStatus(order.getOrderStatus());
        dto.setProposedPrice(order.getProposedPrice());
        dto.setDescription(order.getDescription());
        dto.setAddress(order.getAddress());
        dto.setProposedStartDate(order.getProposedStartDate());
        dto.setService(ServiceDto.mapToDto(order.getService()));
        dto.setYourOffer(OfferDto.mapToDto(specialistOffer));
        dto.setCustomer(CustomerDto.mapToDto(order.getCustomer()));
        dto.setRating(rating);
        return dto;
    }
}
