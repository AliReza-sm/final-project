package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto.OrderResponseDto createOrder(OrderDto.OrderRequestDto requestDto, Long customerId);
    List<OrderDto.OrderResponseDto> findAvailableOrdersForSpecialist(Long specialistId);
    OrderDto.OrderResponseDto findOrderById(Long orderId, String email);
    void markWorkAsStarted(Long orderId, Long customerId);
    void markWorkAsDone(Long orderId, Long customerId);

    void payForOrder(Long orderId, Long customerId);
}
