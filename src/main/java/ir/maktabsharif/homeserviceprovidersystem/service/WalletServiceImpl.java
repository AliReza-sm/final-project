package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.TransactionType;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WalletServiceImpl extends BaseServiceImpl<Wallet, Long> implements WalletService{

    private final WalletRepository walletRepository;
    private final UserService userService;
    private final TransactionService transactionService;

    public WalletServiceImpl(WalletRepository walletRepository, UserService userService, TransactionService transactionService) {
        super(walletRepository);
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public WalletDto.WalletResponseDto findWalletByOwnerEmail(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return WalletDto.mapToDto(user.getWallet());
    }

    @Override
    public void depositToWallet(String email, Double amount) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        user.getWallet().setBalance(user.getWallet().getBalance()+amount);
        transactionService.create(email, amount, TransactionType.DEPOSIT);
        userService.save(user);
        walletRepository.save(user.getWallet());
        WalletDto.mapToDto(user.getWallet());
    }

    @Override
    public WalletDto.WalletResponseDto withdrawFromWallet(String email, Double amount) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        user.getWallet().setBalance(user.getWallet().getBalance()-amount);
        transactionService.create(email, amount, TransactionType.WITHDRAW);
        userService.save(user);
        walletRepository.save(user.getWallet());
        return WalletDto.mapToDto(user.getWallet());
    }

    @Override
    public Double getWalletBalance(String email){
        return findWalletByOwnerEmail(email).getBalance();
    }
}
