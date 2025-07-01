package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        Double proposedPrice,
        String description,
        String address,
        LocalDateTime proposedStartDate,
        OrderStatus orderStatus,
        Service service,
        Customer customer,
        List<Offer> offers
) {
}
