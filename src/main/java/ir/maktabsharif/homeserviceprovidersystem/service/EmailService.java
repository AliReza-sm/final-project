package ir.maktabsharif.homeserviceprovidersystem.service;

public interface EmailService {
    void sendActivationEmail(String to, String token);
}
