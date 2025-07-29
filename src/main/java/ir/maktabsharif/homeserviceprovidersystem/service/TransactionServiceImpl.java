package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Transaction;
import ir.maktabsharif.homeserviceprovidersystem.entity.TransactionType;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionServiceImpl extends BaseServiceImpl<Transaction, Long> implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService) {
        super(transactionRepository);
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Override
    public TransactionDto.TransactionResponseDto create(String email, Double amount,
                                                        TransactionType transactionType){
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Transaction transaction = new Transaction();
        transaction.setWallet(user.getWallet());
        transaction.setAmount(amount);
        transaction.setType(transactionType);
        transaction.setDescription(transactionType.toString() + " to user with id: " + user.getId());
        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionDto.mapToDto(savedTransaction);
    }

    @Override
    public List<TransactionDto.TransactionResponseDto> getTransactionHistory(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return transactionRepository.findByWalletId(user.getWallet().getId())
                .stream()
                .map(TransactionDto::mapToDto)
                .collect(Collectors.toList());
    }
}
