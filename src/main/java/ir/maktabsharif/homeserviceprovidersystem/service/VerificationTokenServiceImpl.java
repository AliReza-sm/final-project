package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.entity.VerificationToken;
import ir.maktabsharif.homeserviceprovidersystem.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken create(User user) {
        VerificationToken verificationToken = new VerificationToken(generateRandomToken(), user);
        return verificationTokenRepository.save(verificationToken);
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void delete(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
