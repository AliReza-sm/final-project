package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Customer customer;
    private Transaction transaction;
    private Wallet wallet;


    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setId(1L);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Test");
        customer.setLastname("Test");
        customer.setEmail("customer@gmail.com");
        customer.setWallet(wallet);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(200D);
        transaction.setWallet(customer.getWallet());

    }

    @Test
    void create() {
        when(userService.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
        TransactionDto.TransactionResponseDto transactionResponseDto = transactionService.create(customer.getEmail(), 200D, TransactionType.DEPOSIT);
        assertNotNull(transactionResponseDto);
        assertEquals(200D, transactionResponseDto.getAmount());
    }

    @Test
    void getTransactionHistory() {
        when(userService.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));
        when(transactionRepository.findByWalletId(1L)).thenReturn(List.of(transaction));
        List<TransactionDto.TransactionResponseDto> transactionHistory = transactionService.getTransactionHistory(customer.getEmail());
        assertNotNull(transactionHistory);
        assertEquals(1L, transactionHistory.size());
        assertEquals(1L, transactionHistory.getFirst().getId());
        assertEquals(200D, transactionHistory.getFirst().getAmount());
    }
}