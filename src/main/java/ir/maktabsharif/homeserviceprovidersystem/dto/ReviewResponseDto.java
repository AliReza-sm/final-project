package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;

public record ReviewResponseDto(
        Long id,
        Integer rating,
        String comment,
        Order order,
        Customer customer,
        Specialist specialist
) {
}
