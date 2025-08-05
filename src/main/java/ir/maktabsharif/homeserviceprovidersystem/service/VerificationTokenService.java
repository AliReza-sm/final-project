package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationToken;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationTokenType;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationToken create(User user, VerificationTokenType tokenType);
    Optional<VerificationToken> findByToken(String token);
    void delete(VerificationToken verificationToken);
}
