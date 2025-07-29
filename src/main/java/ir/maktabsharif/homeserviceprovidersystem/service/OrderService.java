package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService extends BaseService<Order, Long>{

    boolean existByServiceId(Long id);
    OrderDto.OrderResponseDto createOrder(OrderDto.OrderRequestDto requestDto, Long customerId);
    List<OrderDto.OrderResponseDto> findAvailableOrdersForSpecialist(Long specialistId);
    OrderDto.OrderResponseDto findOrderById(Long orderId, String email);
    void markWorkAsStarted(Long orderId, Long customerId);
    void markWorkAsDone(Long orderId, Long customerId);
    void payForOrder(Long orderId, Long customerId);
    Page<OrderDto.CustomerOrderHistoryDto> getOrderHistoryForCustomer(Long customerId, OrderStatus status, Pageable pageable);
    Page<OrderDto.ManagerOrderHistorySummaryDto> getOrderHistoryForManager(OrderFilterDto filter, Pageable pageable);
    OrderDto.ManagerOrderDetailDto getManagerOrderDetail(Long orderId);
    Page<OrderDto.SpecialistOrderHistoryDto> getOrderHistoryForSpecialist(Long specialistId, Pageable pageable);
    OrderDto.SpecialistOrderDetailDto getOrderDetailForSpecialist(Long orderId, Long specialistId);
}
