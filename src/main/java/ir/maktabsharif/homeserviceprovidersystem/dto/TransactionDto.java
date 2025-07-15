package ir.maktabsharif.homeserviceprovidersystem.dto;

import ir.maktabsharif.homeserviceprovidersystem.entity.Transaction;
import ir.maktabsharif.homeserviceprovidersystem.entity.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

public class TransactionDto {

    @Data
    public static class TransactionResponseDto {
        private Long id;
        private Double amount;
        private TransactionType type;
        private String description;
        private LocalDateTime time;
    }

    public static TransactionResponseDto mapToDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.id = transaction.getId();
        dto.amount = transaction.getAmount();
        dto.type = transaction.getType();
        dto.description = transaction.getDescription();
        dto.time = transaction.getTime();
        return dto;
    }
}
