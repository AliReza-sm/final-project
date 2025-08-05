package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TemporaryEmailDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.TemporaryEmail;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.repository.TemporaryEmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemporaryEmailServiceImplTest {

    @Mock
    private TemporaryEmailRepository temporaryEmailRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TemporaryEmailServiceImpl temporaryEmailService;

    private User user;
    private TemporaryEmail temporaryEmail;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("old.email@google.com");

        temporaryEmail = new TemporaryEmail();
        temporaryEmail.setId(1L);
        temporaryEmail.setUser(user);
        temporaryEmail.setEmail("new.email@example.com");
    }

    @Test
    void findByUserId() {
        when(temporaryEmailRepository.findByUserId(1L)).thenReturn(Optional.of(temporaryEmail));
        Optional<TemporaryEmail> result = temporaryEmailService.findByUserId(1L);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(temporaryEmail);
        verify(temporaryEmailRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createTemporaryEmail() {
        String newEmail = "new.email@google.com";
        when(userService.findById(1L)).thenReturn(Optional.of(user));
        when(userService.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(temporaryEmailRepository.save(any(TemporaryEmail.class))).thenAnswer(invocation -> invocation.getArgument(0));
        TemporaryEmailDto.TemporaryEmailResponse response = temporaryEmailService.createTemporaryEmail(1L, newEmail);
        assertThat(response).isNotNull();
        assertThat(response.getOldEmail()).isEqualTo(user.getEmail());
        assertThat(response.getNewEmail()).isEqualTo(newEmail);
        ArgumentCaptor<TemporaryEmail> captor = ArgumentCaptor.forClass(TemporaryEmail.class);
        verify(temporaryEmailRepository).save(captor.capture());
        TemporaryEmail savedEntity = captor.getValue();
        assertThat(savedEntity.getUser()).isEqualTo(user);
        assertThat(savedEntity.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void deleteTemporaryEmail() {
        TemporaryEmail temporaryEmail = new TemporaryEmail();
        temporaryEmail.setId(10L);
        temporaryEmail.setUser(user);
        temporaryEmail.setEmail("new.email@example.com");
        when(temporaryEmailRepository.findByUserId(1L)).thenReturn(Optional.of(temporaryEmail));
        doNothing().when(temporaryEmailRepository).delete(any(TemporaryEmail.class));
        temporaryEmailService.deleteTemporaryEmail(1L);
        verify(temporaryEmailRepository, times(1)).delete(temporaryEmail);
    }
}