package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.SpecialistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/specialists")
@RequiredArgsConstructor

public class SpecialistController {

    private final SpecialistService specialistService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<Void> updateSpecialist(@AuthenticationPrincipal MyUserDetails userDetails,
                                                 @Valid @ModelAttribute SpecialistDto.SpecialistUpdateDto dto) throws IOException {
        specialistService.updateSpecialist(userDetails.getId(), dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<List<SpecialistDto.SpecialistResponseDto>> getAllSpecialists() {
        return ResponseEntity.ok(specialistService.findAllSpecialists());
    }

    @PutMapping("/{specialistId}/approve")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> approveSpecialist(@Valid@PathVariable Long specialistId) {
        specialistService.approveSpecialist(specialistId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{specialistId}/services/{serviceId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> assignSpecialistToService(@PathVariable Long specialistId, @PathVariable Long serviceId) {
        specialistService.assignSpecialistToService(specialistId, serviceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{specialistId}/services/{serviceId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Void> removeSpecialistFromService(@PathVariable Long specialistId, @PathVariable Long serviceId) {
        specialistService.removeSpecialistFromService(specialistId, serviceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rating")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<SpecialistDto.SpecialistRating> getSpecialistRating(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(specialistService.getAverageScore(userDetails.getId()));
    }
}
