package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.TransactionDto;
import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor

public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{email}")
    public ResponseEntity<WalletDto.WalletResponseDto> getMyWallet(@PathVariable String email) {
        return ResponseEntity.ok(walletService.findWalletByOwnerEmail(email));
    }

    @GetMapping("/transactions/{email}")
    public ResponseEntity<List<TransactionDto.TransactionResponseDto>> getWalletHistory(@PathVariable String email) {
        return ResponseEntity.ok(walletService.getWalletHistory(email));
    }

    @GetMapping("/balance/{email}")
    public ResponseEntity<Double> getBalance(@PathVariable String email) {
        return ResponseEntity.ok(walletService.getWalletBalance(email));
    }
}
