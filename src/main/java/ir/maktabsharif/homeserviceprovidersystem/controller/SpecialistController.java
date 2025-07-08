package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.OfferDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.OrderDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.service.SpecialistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/specialists")
@RequiredArgsConstructor

public class SpecialistController {

    private final SpecialistService specialistService;

    @PostMapping("/register")
    public ResponseEntity<SpecialistDto.SpecialistResponseDto> register(@Valid @RequestBody SpecialistDto.SpecialistRequestDto dto) throws IOException {
        SpecialistDto.SpecialistResponseDto newSpecialist = specialistService.register(dto);
        return new ResponseEntity<>(newSpecialist, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateSpecialist(@RequestBody SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        specialistService.updateSpecialist(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{specialistId}/viewAvailableOrders")
    public ResponseEntity<List<OrderDto.OrderResponseDto>> viewAvailableOrders(@PathVariable Long specialistId){
        List<OrderDto.OrderResponseDto> availableOrders = specialistService.viewAvailableOrders(specialistId);
        return new ResponseEntity<>(availableOrders, HttpStatus.OK);
    }

    @PostMapping("/{specialistId}/offers")
    public ResponseEntity<OfferDto.OfferResponseDto> submitOffer(@PathVariable Long specialistId, @RequestBody OfferDto.OfferRequestDto dto) {
        OfferDto.OfferResponseDto createdOffer = specialistService.submitOffer(specialistId, dto);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @GetMapping("/{specialistId}/showWalletBalance")
    public ResponseEntity<Double> showWalletBalance(@PathVariable Long specialistId){
        return new ResponseEntity<>(specialistService.showWalletBalance(specialistId), HttpStatus.OK);
    }
}
