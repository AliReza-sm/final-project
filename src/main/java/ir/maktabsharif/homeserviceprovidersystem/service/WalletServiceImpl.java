package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.TransactionRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.UserRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final UserRepository<User> userRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public WalletDto.WalletResponseDto findWalletByOwnerEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return WalletDto.mapToDto(user.getWallet());
    }


    @Override
    public List<TransactionDto.TransactionResponseDto> getWalletHistory(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return transactionRepository.findByWalletId(user.getWallet().getId())
                .stream()
                .map(TransactionDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getWalletBalance(String email){
        return findWalletByOwnerEmail(email).getBalance();
    }
}
