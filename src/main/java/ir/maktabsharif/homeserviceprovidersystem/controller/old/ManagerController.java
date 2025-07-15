//package ir.maktabsharif.homeserviceprovidersystem.controller.old;
//
//import ir.maktabsharif.homeserviceprovidersystem.dto.ManagerUpdateDto;
//import ir.maktabsharif.homeserviceprovidersystem.dto.ServiceDto;
//import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
//import ir.maktabsharif.homeserviceprovidersystem.service.ManagerService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/manager")
//@RequiredArgsConstructor
//
//public class ManagerController {
//
//    private final ManagerService managerService;
//
//    @PostMapping("/services/create")
//    public ResponseEntity<ServiceDto.ServiceResponseDto> createService(@RequestBody ServiceDto.ServiceRequestDto serviceRequestDto) {
//        ServiceDto.ServiceResponseDto serviceResponseDto = managerService.createService(serviceRequestDto);
//        return new ResponseEntity<>(serviceResponseDto, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/services/update")
//    public ResponseEntity<ServiceDto.ServiceResponseDto> updateService(@RequestBody ServiceDto.ServiceUpdateDto serviceUpdateDto) {
//        ServiceDto.ServiceResponseDto updatedService = managerService.updateService(serviceUpdateDto);
//        return new ResponseEntity<>(updatedService, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/services/delete/{serviceId}")
//    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
//        managerService.deleteService(serviceId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/specialists/{specialistId}/approve")
//    public ResponseEntity<Void> approveSpecialist(@PathVariable Long specialistId) {
//        managerService.approveSpecialist(specialistId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/specialists/{specialistId}/services/{serviceId}/addToService")
//    public ResponseEntity<Void> assignSpecialistToService(@PathVariable Long serviceId, @PathVariable Long specialistId) {
//        managerService.assignSpecialistToService(serviceId, specialistId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/specialists/{specialistId}/services/{serviceId}/removeFromService")
//    public ResponseEntity<Void> removeSpecialistFromService(@PathVariable Long serviceId, @PathVariable Long specialistId) {
//        managerService.removeSpecialistFromService(serviceId, specialistId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/specialists/showAll")
//    public ResponseEntity<List<SpecialistDto.SpecialistResponseDto>> showAllSpecialists() {
//        List<SpecialistDto.SpecialistResponseDto> allSpecialists = managerService.findAllSpecialists();
//        return new ResponseEntity<>(allSpecialists, HttpStatus.OK);
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<Void> updateManagerCredentials(@RequestBody ManagerUpdateDto managerUpdateDto){
//        managerService.updateManagerCredentials(managerUpdateDto);
//        return ResponseEntity.ok().build();
//    }
//}
