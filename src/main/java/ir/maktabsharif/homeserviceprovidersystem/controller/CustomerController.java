package ir.maktabsharif.homeserviceprovidersystem.controller;


import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @PutMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<CustomerDto.CustomerResponseDto> update(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                  @Valid @RequestBody CustomerDto.CustomerUpdateDto dto) {
        CustomerDto.CustomerResponseDto updatedCustomer = customerService.update(userDetails.getId(), dto);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }
}
