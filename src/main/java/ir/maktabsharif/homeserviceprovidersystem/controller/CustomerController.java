package ir.maktabsharif.homeserviceprovidersystem.controller;


import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto.CustomerResponseDto> register(@Valid @RequestBody CustomerDto.CustomerRequestDto dto) {
        CustomerDto.CustomerResponseDto newCustomer = customerService.register(dto);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto.CustomerResponseDto> update(@PathVariable Long customerId, @RequestBody CustomerDto.CustomerUpdateDto dto) {
        CustomerDto.CustomerResponseDto updatedCustomer = customerService.update(customerId, dto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }
}
