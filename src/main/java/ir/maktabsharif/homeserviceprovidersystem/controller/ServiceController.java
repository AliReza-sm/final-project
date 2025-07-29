package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
import ir.maktabsharif.homeserviceprovidersystem.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor

public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<ServiceDto.ServiceResponseDto> createService(@Valid @RequestBody ServiceDto.ServiceRequestDto requestDto) {
        ServiceDto.ServiceResponseDto createdService = serviceService.createService(requestDto);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<ServiceDto.ServiceResponseDto> updateService(@PathVariable Long id, @RequestBody ServiceDto.ServiceRequestDto requestDto) {
        ServiceDto.ServiceResponseDto updatedService = serviceService.updateService(id, requestDto);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public ResponseEntity<ServiceDto.ServiceResponseDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.findServiceById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<List<ServiceDto.ServiceResponseDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAllServices());
    }
}
