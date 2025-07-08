package ir.maktabsharif.homeserviceprovidersystem.config;

import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.CustomerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ManagerRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.ServiceRepository;
import ir.maktabsharif.homeserviceprovidersystem.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Customer customer1 = new Customer();
        customer1.setFirstname("Alice");
        customer1.setLastname("Smith");
        customer1.setEmail("alice@email.com");
        customer1.setPassword("password123");
        customer1.setWallet(new Wallet());
        customer1.getWallet().setUser(customer1);
        customer1.getWallet().setBalance(500.00);

        Customer customer2 = new Customer();
        customer2.setFirstname("Bob");
        customer2.setLastname("Johnson");
        customer2.setEmail("bob@email.com");
        customer2.setPassword("password123");
        customer2.setWallet(new Wallet());
        customer2.getWallet().setUser(customer2);

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        Specialist specialist1 = new Specialist();
        specialist1.setFirstname("Charlie");
        specialist1.setLastname("Brown");
        specialist1.setEmail("charlie@specialist.com");
        specialist1.setPassword("password123");
        specialist1.setAccountStatus(AccountStatus.APPROVED);
        specialist1.setWallet(new Wallet());
        specialist1.getWallet().setUser(specialist1);

        Specialist specialist2 = new Specialist();
        specialist2.setFirstname("Diana");
        specialist2.setLastname("Prince");
        specialist2.setEmail("diana@specialist.com");
        specialist2.setPassword("password123");
        specialist2.setAccountStatus(AccountStatus.APPROVED);
        specialist2.setWallet(new Wallet());
        specialist2.getWallet().setUser(specialist2);

        Specialist specialist3 = new Specialist();
        specialist3.setFirstname("Peter");
        specialist3.setLastname("Parker");
        specialist3.setEmail("peter@specialist.com");
        specialist3.setPassword("password123");
        specialist3.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        specialist3.setWallet(new Wallet());
        specialist3.getWallet().setUser(specialist3);

        specialistRepository.save(specialist1);
        specialistRepository.save(specialist2);
        specialistRepository.save(specialist3);

        Service mainService1 = new Service();
        mainService1.setName("Home Cleaning");
        mainService1.setDescription("General cleaning services for your home.");
        mainService1.setBasePrice(50.0);

        Service mainService2 = new Service();
        mainService2.setName("Plumbing");
        mainService2.setDescription("Fixing leaks and other plumbing issues.");
        mainService2.setBasePrice(80.0);

        serviceRepository.save(mainService1);
        serviceRepository.save(mainService2);

        Service subService1 = new Service();
        subService1.setName("Kitchen Cleaning");
        subService1.setBasePrice(75.0);
        subService1.setParentService(mainService1);
        mainService1.getSubServices().add(subService1);

        Service subService2 = new Service();
        subService2.setName("Leak Repair");
        subService2.setBasePrice(90.0);
        subService2.setParentService(mainService2);
        mainService2.getSubServices().add(subService2);

        serviceRepository.save(subService1);
        serviceRepository.save(subService2);

        specialist1.getSpecialistServices().add(subService1);
        specialist2.getSpecialistServices().add(subService2);
        specialist1.getSpecialistServices().add(subService2);

        specialistRepository.save(specialist1);
        specialistRepository.save(specialist2);

    }
}
