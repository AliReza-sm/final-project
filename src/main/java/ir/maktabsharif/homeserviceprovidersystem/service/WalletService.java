package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;

import java.util.List;

public interface WalletService {

    WalletDto.WalletResponseDto findWalletByOwnerEmail(String email);
//    WalletDto.WalletResponseDto depositToWallet(Double amount, String email);
    List<TransactionDto.TransactionResponseDto> getWalletHistory(String email);
    Double getWalletBalance(String email);

}
