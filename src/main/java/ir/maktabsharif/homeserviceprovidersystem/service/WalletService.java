package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;

public interface WalletService extends BaseService<Wallet, Long> {

    WalletDto.WalletResponseDto findWalletByOwnerEmail(String email);
    void depositToWallet(String email, Double amount);
    WalletDto.WalletResponseDto withdrawFromWallet(String email, Double amount);
    Double getWalletBalance(String email);

}
