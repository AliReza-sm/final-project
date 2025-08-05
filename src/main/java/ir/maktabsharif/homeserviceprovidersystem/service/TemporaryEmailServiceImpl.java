package ir.maktabsharif.homeserviceprovidersystem.service;

import ir.maktabsharif.homeserviceprovidersystem.dto.TemporaryEmailDto;
import ir.maktabsharif.homeserviceprovidersystem.entity.TemporaryEmail;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import ir.maktabsharif.homeserviceprovidersystem.exception.AlreadyExistException;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import ir.maktabsharif.homeserviceprovidersystem.repository.TemporaryEmailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TemporaryEmailServiceImpl extends BaseServiceImpl<TemporaryEmail, Long> implements TemporaryEmailService {

    private final TemporaryEmailRepository temporaryEmailRepository;
    private final UserService userService;

    @Override
    public Optional<TemporaryEmail> findByUserId(Long userId) {
        return temporaryEmailRepository.findByUserId(userId);
    }

    public TemporaryEmailServiceImpl(TemporaryEmailRepository repository, UserService userService) {
        super(repository);
        this.temporaryEmailRepository = repository;
        this.userService = userService;
    }

    @Override
    public TemporaryEmailDto.TemporaryEmailResponse createTemporaryEmail(Long userId, String newEmail) {
        User user = userService.findById(userId).
                orElseThrow(() -> new ResourceNotFoundException("user not found"));
        if (userService.findByEmail(newEmail).isPresent()) {
            throw new AlreadyExistException("Email already exist");
        }
        TemporaryEmail temporaryEmail = new TemporaryEmail();
        temporaryEmail.setEmail(newEmail);
        temporaryEmail.setUser(user);
        TemporaryEmail save = temporaryEmailRepository.save(temporaryEmail);
        return new TemporaryEmailDto.TemporaryEmailResponse(user.getEmail(), save.getEmail());
    }

    @Override
    public void deleteTemporaryEmail(Long userId) {
        TemporaryEmail temporaryEmail = temporaryEmailRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("TemporaryEmail not found"));
        temporaryEmailRepository.delete(temporaryEmail);
    }
}
