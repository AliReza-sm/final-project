package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.AuthenticationDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto.LoginResponseDto> authenticateUser(@Valid @RequestBody AuthenticationDto.LoginRequestDto loginRequest) {
        AuthenticationDto.LoginResponseDto loginResponseDto = authenticationService.loginUser(loginRequest);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationDto.MessageResponse> registerCustomer(@Valid @RequestBody CustomerDto.CustomerRequestDto customerRequest) {
        authenticationService.registerCustomer(customerRequest);
        return ResponseEntity.ok(new AuthenticationDto.MessageResponse("Customer registered successfully. Please check your email to activate your account."));
    }

    @PostMapping(value = "/register/specialist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthenticationDto.MessageResponse> registerSpecialist(@Valid @ModelAttribute SpecialistDto.SpecialistRequestDto signUpRequest) throws IOException {
        authenticationService.registerSpecialist(signUpRequest);
        return ResponseEntity.ok(new AuthenticationDto.MessageResponse("Specialist registered successfully. Please check your email to activate your account."));
    }

    @GetMapping("/activate")
    public ResponseEntity<AuthenticationDto.MessageResponse> activateAccount(@RequestParam("token") String token) {
        authenticationService.activateUser(token);
        return ResponseEntity.ok(new AuthenticationDto.MessageResponse("Account activated successfully"));
    }
}
