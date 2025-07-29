package ir.maktabsharif.homeserviceprovidersystem.config;

import ir.maktabsharif.homeserviceprovidersystem.entity.*;
import ir.maktabsharif.homeserviceprovidersystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final OfferRepository offerRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (customerRepository.count() != 0) {
            return;
        }
        Customer customer1 = createCustomer("Alice", "Smith", "alice@customer.com", "password123", 500.0);
        Customer customer2 = createCustomer("Bob", "Johnson", "bob@customer.com", "password123", 150.0);

        Specialist specialist1 = createSpecialist("Charlie", "Brown", "charlie@specialist.com", "password123", AccountStatus.APPROVED);
        Specialist specialist2 = createSpecialist("Diana", "Prince", "diana@specialist.com", "password123", AccountStatus.APPROVED);
        Specialist specialist3 = createSpecialist("Peter", "Parker", "peter@specialist.com", "password123", AccountStatus.PENDING_APPROVAL);

        Service homeCleaning = createService("Home Cleaning", "General", 50.0, null);
        Service plumbing = createService("Plumbing", "General", 80.0, null);
        Service gardening = createService("Gardening", "General", 40.0, null);
        Service applianceRepair = createService("Appliance Repair", "General", 70.0, null);

        Service kitchenCleaning = createService("Kitchen Cleaning", "General", 75.0, homeCleaning);
        Service leakRepair = createService("Leak Repair", "General", 90.0, plumbing);
        Service lawnMowing = createService("Lawn Mowing", "General", 45.0, gardening);
        Service fridgeRepair = createService("Refrigerator Repair", "General", 100.0, applianceRepair);

        specialist1.getSpecialistServices().addAll(List.of(kitchenCleaning, leakRepair, lawnMowing));
        specialist2.getSpecialistServices().addAll(List.of(leakRepair, fridgeRepair));
        specialistRepository.saveAll(List.of(specialist1, specialist2));

        Order order1 = createOrder(customer1, lawnMowing, 50.0, "All", "eram", LocalDateTime.now().plusDays(3), OrderStatus.WAITING_FOR_SPECIALIST_OFFERS);
        Order order2 = createOrder(customer2, leakRepair, 120.0, "All", "eram", LocalDateTime.now().plusDays(1), OrderStatus.WAITING_FOR_OFFER_SELECTION);
        Offer offer2_1 = createOffer(specialist1, order2, 130.0, 2, order2.getProposedStartDate().plusHours(1), OfferStatus.PENDING);
        Offer offer2_2 = createOffer(specialist2, order2, 125.0, 3, order2.getProposedStartDate().plusHours(2), OfferStatus.PENDING);
        order2.getOffers().addAll(List.of(offer2_1, offer2_2));
        orderRepository.save(order2);
        Order order3 = createOrder(customer1, fridgeRepair, 110.0, "All", "eram", LocalDateTime.now().plusHours(4), OrderStatus.WAITING_FOR_SPECIALIST_TO_ARRIVE);
        Offer offer3_1 = createOffer(specialist2, order3, 110.0, 4, order3.getProposedStartDate(), OfferStatus.ACCEPTED);
        order3.setSelectedOffer(offer3_1);
        orderRepository.save(order3);

        Order order4 = createOrder(customer2, kitchenCleaning, 80.0, "All", "eram", LocalDateTime.now().minusDays(5), OrderStatus.PAID);
        order4.setWorkCompletedDate(LocalDateTime.now().minusDays(4));
        Offer offer4_1 = createOffer(specialist1, order4, 85.0, 3, order4.getProposedStartDate(), OfferStatus.ACCEPTED);
        order4.setSelectedOffer(offer4_1);
        orderRepository.save(order4);
        Review review4 = createReview(customer2, specialist1, order4, 5, "Good job");
        specialist1.setSumScore(5.0);
        specialist1.setNumberOfReviews(1);
        specialist1.setAverageScore(5.0);
        specialistRepository.save(specialist1);

        Order order5 = createOrder(customer1, leakRepair, 150.0, "All", "eram", LocalDateTime.now().minusHours(2), OrderStatus.DONE);
        order5.setWorkCompletedDate(null);
        Offer offer5_1 = createOffer(specialist2, order5, 150.0, 2, order5.getProposedStartDate(), OfferStatus.ACCEPTED);
        order5.setSelectedOffer(offer5_1);
        orderRepository.save(order5);
    }

    private Customer createCustomer(String firstname, String lastname, String email, String password, double balance) {
        Customer c = new Customer();
        c.setFirstname(firstname);
        c.setLastname(lastname);
        c.setEmail(email);
        c.setPassword(passwordEncoder.encode(password));
        c.setRole(Role.CUSTOMER);
        c.setEnabled(true);
        Wallet wallet = new Wallet();
        wallet.setBalance(balance);
        wallet.setUser(c);
        c.setWallet(wallet);
        return customerRepository.save(c);
    }

    private Specialist createSpecialist(String firstname, String lastname, String email, String password, AccountStatus status) {
        Specialist s = new Specialist();
        s.setFirstname(firstname);
        s.setLastname(lastname);
        s.setEmail(email);
        s.setPassword(passwordEncoder.encode(password));
        s.setRole(Role.SPECIALIST);
        s.setEnabled(true);
        s.setAccountStatus(status);
        Wallet wallet = new Wallet();
        wallet.setUser(s);
        s.setWallet(wallet);
        return specialistRepository.save(s);
    }

    private Service createService(String name, String desc, double price, Service parent) {
        Service s = new Service();
        s.setName(name);
        s.setDescription(desc);
        s.setBasePrice(price);
        if (parent != null) {
            s.setParentService(parent);
        }
        return serviceRepository.save(s);
    }

    private Order createOrder(Customer customer, Service service, double price, String desc, String address, LocalDateTime date, OrderStatus status) {
        Order o = new Order();
        o.setCustomer(customer);
        o.setService(service);
        o.setProposedPrice(price);
        o.setDescription(desc);
        o.setAddress(address);
        o.setProposedStartDate(date);
        o.setOrderStatus(status);
        return orderRepository.save(o);
    }

    private Offer createOffer(Specialist specialist, Order order, double price, int hours, LocalDateTime date, OfferStatus status) {
        Offer offer = new Offer();
        offer.setSpecialist(specialist);
        offer.setOrder(order);
        offer.setProposedPrice(price);
        offer.setTimeToEndTheJobInHours(hours);
        offer.setProposedStartTime(date);
        offer.setOfferStatus(status);
        return offerRepository.save(offer);
    }

    private Review createReview(Customer customer, Specialist specialist, Order order, int rating, String comment) {
        Review r = new Review();
        r.setCustomer(customer);
        r.setSpecialist(specialist);
        r.setOrder(order);
        r.setRating(rating);
        r.setComment(comment);
        return reviewRepository.save(r);
    }
}
