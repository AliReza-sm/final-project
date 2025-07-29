package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("orderStatus"), status);
    }

    public static Specification<Order> forService(Long serviceId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("service").get("id"), serviceId);
    }

    public static Specification<Order> forCustomer(Long customerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }

    public static Specification<Order> forSpecialist(Long specialistId) {
        return (root, query, criteriaBuilder) -> {
            Join<Order, Offer> offerJoin = root.join("selectedOffer");
            return criteriaBuilder.equal(offerJoin.get("specialist").get("id"), specialistId);
        };
    }

    public static Specification<Order> inDateRange(LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("orderCreatedDate"), start, end);
    }

}
