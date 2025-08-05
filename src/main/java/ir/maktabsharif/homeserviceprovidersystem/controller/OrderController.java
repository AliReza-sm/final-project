package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderFilterDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderDto.OrderResponseDto> createOrder(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                 @Valid @RequestBody OrderDto.OrderRequestDto requestDto) {
        OrderDto.OrderResponseDto createdOrder = orderService.createOrder(requestDto, userDetails.getId());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/available")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<List<OrderDto.OrderResponseDto>> getAvailableOrders(@AuthenticationPrincipal MyUserDetails userDetails) {
        List<OrderDto.OrderResponseDto> orders = orderService.findAvailableOrdersForSpecialist(userDetails.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'SPECIALIST')")
    public ResponseEntity<OrderDto.OrderResponseDto> getOrderById(@PathVariable Long id,
                                                                  @AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(orderService.findOrderById(id, userDetails.getUsername()));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Void> markWorkAsStarted(@PathVariable Long id,
                                                  @AuthenticationPrincipal MyUserDetails userDetails) {
        orderService.markWorkAsStarted(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/end")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Void> markWorkAsDone(@PathVariable Long id,
                                               @AuthenticationPrincipal MyUserDetails userDetails) {
        orderService.markWorkAsDone(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Void> payForOrder(@PathVariable Long id,
                                            @AuthenticationPrincipal MyUserDetails userDetails) {
        orderService.payForOrder(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history/customer")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Page<OrderDto.CustomerOrderHistoryDto>> getCustomerOrderHistory(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable) {
        Page<OrderDto.CustomerOrderHistoryDto> history = orderService.getOrderHistoryForCustomer(userDetails.getId(), status, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/specialist")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<Page<OrderDto.SpecialistOrderHistoryDto>> getSpecialistOrderHistory(
            @AuthenticationPrincipal MyUserDetails userDetails,
            Pageable pageable) {
        Page<OrderDto.SpecialistOrderHistoryDto> history = orderService.getOrderHistoryForSpecialist(userDetails.getId(), pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/manager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Page<OrderDto.ManagerOrderHistorySummaryDto>> getManagerOrderHistory(
            @Valid OrderFilterDto filter,
            Pageable pageable) {
        Page<OrderDto.ManagerOrderHistorySummaryDto> history = orderService.getOrderHistoryForManager(filter, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{orderId}/detail/manager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<OrderDto.ManagerOrderDetailDto> getManagerOrderDetail(@PathVariable Long orderId) {
        OrderDto.ManagerOrderDetailDto orderDetail = orderService.getManagerOrderDetail(orderId);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/{orderId}/detail/specialist")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<OrderDto.SpecialistOrderDetailDto> getSpecialistOrderDetail(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @PathVariable Long orderId) {
        OrderDto.SpecialistOrderDetailDto orderDetail = orderService.getOrderDetailForSpecialist(orderId, userDetails.getId());
        return ResponseEntity.ok(orderDetail);
    }
}
