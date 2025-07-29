package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationToken create(User user);
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
}
