package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.UserDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ManagerRepository managerRepository;

    public UserDto.LoginResponseDto login(UserDto.LoginRequestDto loginRequestDto) {
        Optional<? extends User> loggedInUser = managerRepository.findByEmail(loginRequestDto.getEmail());
        String role = "manager";
        if (loggedInUser.isEmpty()) {
            loggedInUser = specialistRepository.findByEmail(loginRequestDto.getEmail());
            role = "specialist";
        }
        if (loggedInUser.isEmpty()) {
            loggedInUser = customerRepository.findByEmail(loginRequestDto.getEmail());
            role = "customer";
        }
        if (loggedInUser.isEmpty()) {
            throw new ResourceNotFoundException("invalid email or password");
        }
        if (loggedInUser.get().getPassword().equals(loginRequestDto.getPassword())){
           UserDto.LoginResponseDto loginResponseDto = new UserDto.LoginResponseDto();
           loginResponseDto.setId(loggedInUser.get().getId());
           loginResponseDto.setEmail(loggedInUser.get().getEmail());
           loginResponseDto.setRole(role);
           return loginResponseDto;
        } else {
            throw new ResourceNotFoundException("invalid email or password");
        }
    }
}
