package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor

public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<OrderDto.OrderResponseDto> createOrder(@PathVariable Long customerId, @Valid @RequestBody OrderDto.OrderRequestDto requestDto) {
        OrderDto.OrderResponseDto createdOrder = orderService.createOrder(requestDto, customerId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/available/{specialistId}")
    public ResponseEntity<List<OrderDto.OrderResponseDto>> getAvailableOrders(@PathVariable Long specialistId) {
        List<OrderDto.OrderResponseDto> orders = orderService.findAvailableOrdersForSpecialist(specialistId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}/{userEmail}")
    public ResponseEntity<OrderDto.OrderResponseDto> getOrderById(@PathVariable Long id, @PathVariable String userEmail) {
        return ResponseEntity.ok(orderService.findOrderById(id, userEmail));
    }

    @PutMapping("/{id}/start/{customerId}")
    public ResponseEntity<Void> markWorkAsStarted(@PathVariable Long id, @PathVariable Long customerId) {
        orderService.markWorkAsStarted(id, customerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/end/{customerId}")
    public ResponseEntity<Void> markWorkAsDone(@PathVariable Long id, @PathVariable Long customerId) {
        orderService.markWorkAsDone(id, customerId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/pay/{customerId}")
    public ResponseEntity<Void> payForOrder(@PathVariable Long id, @PathVariable Long customerId) {
        orderService.payForOrder(id, customerId);
        return ResponseEntity.ok().build();
    }
}
