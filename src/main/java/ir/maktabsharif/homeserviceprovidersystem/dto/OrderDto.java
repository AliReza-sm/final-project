package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
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
}
