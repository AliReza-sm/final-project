package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.AuthenticationDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.CustomerDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.SpecialistDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.VerificationTokenRepository;
import ir.maktabsharif.homeserviceprovidersystem.security.JwtUtil;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private CustomerService customerService;
    @Mock
    private SpecialistService specialistService;
    @Mock
    private VerificationTokenServiceImpl verificationTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationServiceImpl authService;

    private User user;
    private AuthenticationDto.LoginRequestDto loginRequest;
    private CustomerDto.CustomerRequestDto customerRegistrationRequest;
    private SpecialistDto.SpecialistRequestDto specialistRegistrationRequest;
    private VerificationToken verificationToken;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);

        loginRequest = new AuthenticationDto.LoginRequestDto();
        loginRequest.setEmail("user@gmail.com");
        loginRequest.setPassword("password123");

        customerRegistrationRequest = new CustomerDto.CustomerRequestDto();
        customerRegistrationRequest.setFirstName("ali");
        customerRegistrationRequest.setLastName("reza");
        customerRegistrationRequest.setEmail("newcustomer@example.com");
        customerRegistrationRequest.setPassword("password123");

        specialistRegistrationRequest = new SpecialistDto.SpecialistRequestDto();
        specialistRegistrationRequest.setFirstName("Jane");
        specialistRegistrationRequest.setLastName("Smith");
        specialistRegistrationRequest.setEmail("newspecialist@example.com");
        specialistRegistrationRequest.setPassword("password456");
        specialistRegistrationRequest.setProfilePhotoData(new MockMultipartFile("photo", "photo.jpg", "image/jpeg", "some-image-bytes".getBytes()));

        verificationToken = new VerificationToken();
        verificationToken.setId(1L);
        verificationToken.setToken("aa");
        verificationToken.setVerificationTokenType(VerificationTokenType.REGISTER);
    }


    @Test
    void loginUser() {
        Authentication authentication = mock(Authentication.class);
        MyUserDetails userDetails = MyUserDetails.build(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwtToken");
        AuthenticationDto.LoginResponseDto response = authService.loginUser(loginRequest);
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwtToken");
        assertThat(response.getEmail()).isEqualTo("user@gmail.com");
        assertThat(response.getRole()).isEqualTo("CUSTOMER");
    }

    @Test
    void registerCustomer() {
        when(userService.findByEmail(customerRegistrationRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(customerRegistrationRequest.getPassword())).thenReturn("encodedPassword");
        when(verificationTokenService.create(any(), any())).thenReturn(verificationToken);
        authService.registerCustomer(customerRegistrationRequest);
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerService).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();

        assertThat(savedCustomer.getEmail()).isEqualTo(customerRegistrationRequest.getEmail());
        assertThat(savedCustomer.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedCustomer.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(savedCustomer.isEnabled()).isFalse();
    }

    @Test
    void registerSpecialist() throws IOException {
        when(userService.findByEmail(specialistRegistrationRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(specialistRegistrationRequest.getPassword())).thenReturn("encodedSpecialistPassword");
        when(verificationTokenService.create(any(), any())).thenReturn(verificationToken);
        authService.registerSpecialist(specialistRegistrationRequest);
        ArgumentCaptor<Specialist> specialistCaptor = ArgumentCaptor.forClass(Specialist.class);
        verify(specialistService).save(specialistCaptor.capture());
        Specialist savedSpecialist = specialistCaptor.getValue();
        assertThat(savedSpecialist.getEmail()).isEqualTo(specialistRegistrationRequest.getEmail());
        assertThat(savedSpecialist.getPassword()).isEqualTo("encodedSpecialistPassword");
        assertThat(savedSpecialist.getRole()).isEqualTo(Role.SPECIALIST);
        assertThat(savedSpecialist.isEnabled()).isFalse();
        assertThat(savedSpecialist.getAccountStatus()).isEqualTo(AccountStatus.NEW);
    }

    @Test
    void activateUser() {
        String token = "token";
        VerificationToken verificationToken = new VerificationToken(token, user, VerificationTokenType.REGISTER);
        when(verificationTokenService.findByToken(token)).thenReturn(Optional.of(verificationToken));
        authService.activateUser(token);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).save(userCaptor.capture());
        User activatedUser = userCaptor.getValue();
        assertThat(activatedUser.isEnabled()).isTrue();
        verify(verificationTokenService).delete(verificationToken);
    }

    @Test
    void activateUser_ExpiredToken() {
        String token = "expired-token";
        VerificationToken verificationToken = new VerificationToken(token, user, VerificationTokenType.REGISTER);
        verificationToken.setExpiryDate(LocalDateTime.now().minusHours(2));
        when(verificationTokenService.findByToken(token)).thenReturn(Optional.of(verificationToken));
        assertThrows(RuntimeException.class, () -> {
            authService.activateUser(token);
        });
        verify(verificationTokenService).delete(verificationToken);
    }
}