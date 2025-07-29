package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.AuthenticationDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationDto.LoginResponseDto loginUser(AuthenticationDto.LoginRequestDto loginRequestDto);

    void registerCustomer(CustomerDto.CustomerRequestDto dto);

    void registerSpecialist(SpecialistDto.SpecialistRequestDto dto) throws IOException;

    void activateUser(String token);
}
