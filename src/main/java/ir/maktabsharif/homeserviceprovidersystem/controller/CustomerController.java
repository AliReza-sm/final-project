package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.*;
import ir.maktabsharif.homeserviceprovidersystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<CustomerDto.CustomerResponseDto> register(@Valid @RequestBody CustomerDto.CustomerRequestDto dto) {
        CustomerDto.CustomerResponseDto newCustomer = customerService.register(dto);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerDto.CustomerResponseDto> update(@PathVariable Long customerId, @RequestBody CustomerDto.CustomerUpdateDto dto) {
        CustomerDto.CustomerResponseDto updatedCustomer = customerService.update(customerId, dto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @GetMapping("/showAllServices")
    public ResponseEntity<List<ServiceDto.ServiceResponseDto>> showAllServices() {
        List<ServiceDto.ServiceResponseDto> serviceResponseDtos = customerService.showAllServices();
        return new ResponseEntity<>(serviceResponseDtos, HttpStatus.FOUND);
    }

    @PostMapping("/{customerId}/orders/create")
    public ResponseEntity<OrderDto.OrderResponseDto> createOrder(@PathVariable Long customerId,
                                                                 @Valid @RequestBody OrderDto.OrderRequestDto dto) {
        OrderDto.OrderResponseDto createdOrder = customerService.createOrder(customerId, dto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}/order/{orderId}/offer/{offerId}/selectOfferForOrder")
    public ResponseEntity<Void> selectOfferForOrder(@PathVariable Long customerId,
                                                    @PathVariable Long orderId,
                                                    @PathVariable Long offerId) {
        customerService.selectOfferForOrder(customerId, orderId, offerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{customerId}/order/{orderId}/payForOrder")
    public ResponseEntity<Void> payForOrder(@PathVariable Long customerId, @PathVariable Long orderId){
        customerService.payForOrder(customerId, orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{customerId}/leaveReview")
    public ResponseEntity<ReviewDto.ReviewResponseDto> leaveReview(@PathVariable Long customerId, @RequestBody ReviewDto.ReviewRequestDto dto){
        ReviewDto.ReviewResponseDto reviewResponseDto = customerService.leaveReview(customerId, dto);
        return new ResponseEntity<>(reviewResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}/showBalance")
    public ResponseEntity<Double> showWalletBalance(@PathVariable Long customerId){
        Double balance = customerService.showWalletBalance(customerId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PutMapping("/{customerId}/addFund/{amount}")
    public ResponseEntity<Void> addFundsToWallet(@PathVariable Long customerId, @PathVariable Double amount){
        customerService.addFundsToWallet(customerId, amount);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/orders/{orderId}/offers")
    public ResponseEntity<List<OfferDto.OfferResponseDto>> viewOrderOffers(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "price") String sortBy) {
        List<OfferDto.OfferResponseDto> offers = customerService.viewOrderOffers(customerId, orderId, sortBy);
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/{customerId}/orders/{orderId}/startWork")
    public ResponseEntity<Void> announceWorkStarted(@PathVariable Long customerId, @PathVariable Long orderId) {
        customerService.announceWorkStart(customerId, orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{customerId}/orders/{orderId}/endWork")
    public ResponseEntity<Void> announceWorkEnd(@PathVariable Long customerId, @PathVariable Long orderId) {
        customerService.announceWorkEnd(customerId, orderId);
        return ResponseEntity.ok().build();
    }
}
