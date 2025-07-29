package ir.maktabsharif.homeserviceprovidersystem.config;

import ir.maktabsharif.homeserviceprovidersystem.entity.Manager;
import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import ir.maktabsharif.homeserviceprovidersystem.entity.Wallet;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ManagerSeeder implements CommandLineRunner {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${manager.default-firstname}")
    private String defaultFirstname;

    @Value("${manager.default-lastname}")
    private String defaultLastname;

    @Value("${manager.default-email}")
    private String defaultEmail;

    @Value("${manager.default-password}")
    private String defaultPassword;

    public ManagerSeeder(ManagerRepository managerRepository, PasswordEncoder passwordEncoder) {
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (managerRepository.findAll().isEmpty()) {
            createDefaultManager();
        }
    }

    private void createDefaultManager() {
        Manager manager = new Manager();
        manager.setFirstname(defaultFirstname);
        manager.setLastname(defaultLastname);
        manager.setEmail(defaultEmail);
        manager.setPassword(passwordEncoder.encode(defaultPassword));
        manager.setRole(Role.MANAGER);
        Wallet wallet = new Wallet();
        wallet.setUser(manager);
        manager.setWallet(wallet);
        managerRepository.save(manager);
    }
}
