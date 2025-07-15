package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Transaction;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;
import ir.maktabsharif.homeserviceprovidersystem.repository.TransactionRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private UserRepository<User> userRepository;
    @Mock
    private TransactionRepository transactionRepository;

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
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        WalletDto.WalletResponseDto result = walletService.findWalletByOwnerEmail("user@gmail.com");
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(100.0, result.getBalance());
    }

    @Test
    void getWalletHistory() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(transactionRepository.findByWalletId(1L)).thenReturn(List.of(new Transaction()));
        List<TransactionDto.TransactionResponseDto> result = walletService.getWalletHistory("customer@gmail.com");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getWalletBalance(){
        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
        WalletDto.WalletResponseDto result = walletService.findWalletByOwnerEmail("user@gmail.com");
        assertNotNull(result);
        assertEquals(100.0, result.getBalance());
    }

}