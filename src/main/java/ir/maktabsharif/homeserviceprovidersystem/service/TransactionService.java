package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Transaction;
import ir.maktabsharif.homeserviceprovidersystem.entity.TransactionType;

import java.util.List;

public interface TransactionService extends BaseService<Transaction, Long> {

    TransactionDto.TransactionResponseDto create(String email, Double amount, TransactionType transactionType);
    List<TransactionDto.TransactionResponseDto> getTransactionHistory(String email);
}
