package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.AuthenticationDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.security.JwtUtil;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@org.springframework.stereotype.Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final SpecialistService specialistService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Override
    public AuthenticationDto.LoginResponseDto loginUser(AuthenticationDto.LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        return new AuthenticationDto.LoginResponseDto(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Override
    public void registerCustomer(CustomerDto.CustomerRequestDto dto) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            throw new AlreadyExistException("Email is already in use");
        }

        Customer customer = new Customer();
        customer.setFirstname(dto.getFirstName());
        customer.setLastname(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setRole(Role.CUSTOMER);
        Wallet wallet = new Wallet();
        wallet.setUser(customer);
        customer.setWallet(wallet);

        customerService.save(customer);
        VerificationToken verificationToken = verificationTokenService.create(customer);
        emailService.sendActivationEmail(customer.getEmail(), verificationToken.getToken());
    }

    @Override
    public void registerSpecialist(SpecialistDto.SpecialistRequestDto dto) throws IOException {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            throw new AlreadyExistException("Email is already in use");
        }

        Specialist specialist = new Specialist();
        specialist.setFirstname(dto.getFirstName());
        specialist.setLastname(dto.getLastName());
        specialist.setEmail(dto.getEmail());
        specialist.setPassword(passwordEncoder.encode(dto.getPassword()));
        specialist.setRole(Role.SPECIALIST);
        specialist.setSpecialistStatus(SpecialistStatus.ACTIVE);
        specialist.setAccountStatus(AccountStatus.NEW);
        if (dto.getProfilePhotoData() != null && !dto.getProfilePhotoData().isEmpty()) {
            specialist.setProfilePhotoBytes(dto.getProfilePhotoData().getBytes());
        }
        Wallet wallet = new Wallet();
        wallet.setUser(specialist);
        specialist.setWallet(wallet);
        specialistService.save(specialist);
        VerificationToken verificationToken = verificationTokenService.create(specialist);
        emailService.sendActivationEmail(specialist.getEmail(), verificationToken.getToken());
    }

    @Override
    public void activateUser(String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid activation token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenService.delete(verificationToken);
            throw new RuntimeException("Activation token has expired");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);

        if (user instanceof Specialist specialist) {
            if (specialist.getProfilePhotoBytes() != null) {
                specialist.setAccountStatus(AccountStatus.PENDING_APPROVAL);
            } else {
                specialist.setAccountStatus(AccountStatus.NEW);
            }
        }
        userService.save(user);
        verificationTokenService.delete(verificationToken);
    }
}
