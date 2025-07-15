package ir.maktabsharif.homeserviceprovidersystem.config;

import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Customer customer1 = new Customer();
        customer1.setFirstname("Alice");
        customer1.setLastname("Smith");
        customer1.setEmail("alice@email.com");
        customer1.setPassword("password123");
        customer1.setRole(Role.CUSTOMER);
        customer1.setWallet(new Wallet());
        customer1.getWallet().setUser(customer1);
        customer1.getWallet().setBalance(500.00);

        Customer customer2 = new Customer();
        customer2.setFirstname("Bob");
        customer2.setLastname("Johnson");
        customer2.setEmail("bob@email.com");
        customer2.setPassword("password123");
        customer2.setRole(Role.CUSTOMER);
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
        specialist1.setRole(Role.SPECIALIST);
        specialist1.setWallet(new Wallet());
        specialist1.getWallet().setUser(specialist1);

        Specialist specialist2 = new Specialist();
        specialist2.setFirstname("Diana");
        specialist2.setLastname("Prince");
        specialist2.setEmail("diana@specialist.com");
        specialist2.setPassword("password123");
        specialist2.setAccountStatus(AccountStatus.APPROVED);
        specialist2.setRole(Role.SPECIALIST);
        specialist2.setWallet(new Wallet());
        specialist2.getWallet().setUser(specialist2);

        Specialist specialist3 = new Specialist();
        specialist3.setFirstname("Peter");
        specialist3.setLastname("Parker");
        specialist3.setEmail("peter@specialist.com");
        specialist3.setPassword("password123");
        specialist3.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        specialist3.setRole(Role.SPECIALIST);
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

        Order order1 = new Order();
        order1.setCustomer(customer1);
        order1.setService(subService2);
        order1.setProposedPrice(100.0);
        order1.setDescription("aaaa");
        order1.setAddress("sa");
        order1.setProposedStartDate(LocalDateTime.now().plusDays(2));
        order1.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
        orderRepository.save(order1);


        Order order2 = new Order();
        order2.setCustomer(customer2);
        order2.setService(subService1);
        order2.setProposedPrice(80.0);
        order2.setDescription("ds");
        order2.setAddress("gd");
        order2.setProposedStartDate(LocalDateTime.now().minusDays(5));
        order2.setOrderStatus(OrderStatus.DONE);
        order2.setWorkCompletedDate(LocalDateTime.now().minusDays(4));
        orderRepository.save(order2);

        Offer offer2 = new Offer();
        offer2.setSpecialist(specialist1);
        offer2.setOrder(order2);
        offer2.setProposedPrice(80.0);
        offer2.setTimeToEndTheJobInHours(3);
        offer2.setProposedStartTime(order2.getProposedStartDate());
        offer2.setOfferStatus(OfferStatus.ACCEPTED);
        offerRepository.save(offer2);
        order2.setSelectedOffer(offer2);
        orderRepository.save(order2);

        Review review = new Review();
        review.setCustomer(customer2);
        review.setSpecialist(specialist1);
        review.setOrder(order2);
        review.setRating(5);
        review.setComment("fine");
        reviewRepository.save(review);

        specialist1.setSumScore(5.0);
        specialist1.setNumberOfReviews(1);
        specialist1.setAverageScore(5.0);
        specialistRepository.save(specialist1);

    }
}
