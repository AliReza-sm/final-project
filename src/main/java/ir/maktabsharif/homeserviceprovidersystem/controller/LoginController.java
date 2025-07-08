package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor

public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<UserDto.LoginResponseDto> login (@RequestBody UserDto.LoginRequestDto loginRequestDto) {
        UserDto.LoginResponseDto loginResponseDto = loginService.login(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
