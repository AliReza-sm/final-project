package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationTokenType;

public interface EmailService {
    void sendActivationEmail(String to, String token, VerificationTokenType verificationTokenType);
}
