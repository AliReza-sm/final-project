package ir.maktabsharif.homeserviceprovidersystem.controller;

import ir.maktabsharif.homeserviceprovidersystem.dto.WalletDto;
import ir.maktabsharif.homeserviceprovidersystem.security.MyUserDetails;
import ir.maktabsharif.homeserviceprovidersystem.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor

public class WalletController {

    private final WalletService walletService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'SPECIALIST', 'MANAGER')")
    public ResponseEntity<WalletDto.WalletResponseDto> getMyWallet(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(walletService.findWalletByOwnerEmail(userDetails.getUsername()));
    }

    @GetMapping("/balance")
    @PreAuthorize("hasAuthority('SPECIALIST')")
    public ResponseEntity<Double> getBalance(@AuthenticationPrincipal MyUserDetails userDetails) {
        return ResponseEntity.ok(walletService.getWalletBalance(userDetails.getUsername()));
    }
}
