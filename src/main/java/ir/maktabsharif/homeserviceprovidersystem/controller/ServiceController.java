package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor

public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<ServiceDto.ServiceResponseDto> createService(@Valid @RequestBody ServiceDto.ServiceRequestDto requestDto) {
        ServiceDto.ServiceResponseDto createdService = serviceService.createService(requestDto);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto.ServiceResponseDto> updateService(@PathVariable Long id, @RequestBody ServiceDto.ServiceRequestDto requestDto) {
        ServiceDto.ServiceResponseDto updatedService = serviceService.updateService(id, requestDto);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto.ServiceResponseDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceDto.ServiceResponseDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll());
    }
}
