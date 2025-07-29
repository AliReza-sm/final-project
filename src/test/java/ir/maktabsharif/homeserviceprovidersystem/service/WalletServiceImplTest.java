package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private UserService userService;
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Customer customer;
    private Wallet wallet;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        wallet = new Wallet();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setWallet(wallet);
        wallet.setId(1L);
        wallet.setBalance(100.0);
        wallet.setUser(user);
    }

    @Test
    void findWalletByOwnerEmail() {
        when(userService.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        WalletDto.WalletResponseDto result = walletService.findWalletByOwnerEmail("user@gmail.com");
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(100.0, result.getBalance());
    }

    @Test
    void depositToWallet() {
        when(userService.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        walletService.depositToWallet(user.getEmail(), 200D);
        assertEquals(300D, wallet.getBalance());
    }

    @Test
    void withdrawFromWallet() {
        when(userService.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        walletService.withdrawFromWallet(user.getEmail(), 100D);
        assertEquals(0D, wallet.getBalance());
    }

    @Test
    void getWalletBalance(){
        when(userService.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        WalletDto.WalletResponseDto result = walletService.findWalletByOwnerEmail("user@gmail.com");
        assertNotNull(result);
        assertEquals(100.0, result.getBalance());
    }

}