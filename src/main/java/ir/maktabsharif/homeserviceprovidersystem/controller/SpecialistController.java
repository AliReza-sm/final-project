package ir.maktabsharif.homeserviceprovidersystem.controller;

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

    @PostMapping
    public ResponseEntity<SpecialistDto.SpecialistResponseDto> register(@Valid @RequestBody SpecialistDto.SpecialistRequestDto dto) throws IOException {
        SpecialistDto.SpecialistResponseDto newSpecialist = specialistService.register(dto);
        return new ResponseEntity<>(newSpecialist, HttpStatus.CREATED);
    }

    @PutMapping("/{specialistId}")
    public ResponseEntity<Void> updateSpecialist(@PathVariable Long specialistId, @RequestBody SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        specialistService.updateSpecialist(specialistId, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SpecialistDto.SpecialistResponseDto>> getAllSpecialists() {
        return ResponseEntity.ok(specialistService.findAllSpecialists());
    }

    @PutMapping("/{specialistId}/approve")
    public ResponseEntity<Void> approveSpecialist(@Valid@PathVariable Long specialistId) {
        specialistService.approveSpecialist(specialistId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{specialistId}/services/{serviceId}")
    public ResponseEntity<Void> assignSpecialistToService(@PathVariable Long specialistId, @PathVariable Long serviceId) {
        specialistService.assignSpecialistToService(specialistId, serviceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{specialistId}/services/{serviceId}")
    public ResponseEntity<Void> removeSpecialistFromService(@PathVariable Long specialistId, @PathVariable Long serviceId) {
        specialistService.removeSpecialistFromService(specialistId, serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{specialistId}")
    public ResponseEntity<List<SpecialistDto.SpecialistOrderHistoryDto>> getSpecialistOrderHistory(@PathVariable Long specialistId) {
        return ResponseEntity.ok(specialistService.getOrderHistory(specialistId));
    }

}
