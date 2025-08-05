package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TemporaryEmailDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.TemporaryEmail;

import java.util.Optional;

public interface TemporaryEmailService extends BaseService<TemporaryEmail, Long> {

    Optional<TemporaryEmail> findByUserId(Long userId);
    TemporaryEmailDto.TemporaryEmailResponse createTemporaryEmail(Long userId, String newEmail);
    void deleteTemporaryEmail(Long userId);
}
