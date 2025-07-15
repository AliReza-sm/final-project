package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;
import jakarta.validation.constraints.Positive;
import lombok.Data;

public class WalletDto {

    @Data
    public static class DepositRequestDto {
        @Positive(message = "Amount must be positive")
        private Double amount;
    }

    @Data
    public static class WalletResponseDto {
        private Long id;
        private Double balance;
        private Long userId;
    }

    public static WalletResponseDto mapToDto(Wallet wallet) {
        if (wallet == null) { return null; }
        WalletResponseDto dto = new WalletResponseDto();
        dto.id = wallet.getId();
        dto.balance = wallet.getBalance();
        dto.setUserId(wallet.getUser().getId());
        return dto;
    }
}
