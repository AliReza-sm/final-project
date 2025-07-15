package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Role;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), role);
    }

    public static Specification<User> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> hasService(String serviceName) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            query.distinct(true);
            Join<User, Specialist> specialistJoin = root.join("specialist", JoinType.INNER);
            Join<Specialist, Service> serviceJoin = specialistJoin.join("specialistServices");
            return criteriaBuilder.equal(serviceJoin.get("name"), serviceName);
        };
    }

        public static Specification<User> scoreIsBetween(Double minScore, Double maxScore) {
            return (root, query, criteriaBuilder) -> {
                Join<User, Specialist> specialistJoin = root.join("specialist", JoinType.INNER);
                if (minScore != null && maxScore != null) {
                    return criteriaBuilder.between(specialistJoin.get("averageScore"), minScore, maxScore);
                } else if (minScore != null) {
                    return criteriaBuilder.greaterThanOrEqualTo(specialistJoin.get("averageScore"), minScore);
                } else if (maxScore != null) {
                    return criteriaBuilder.lessThanOrEqualTo(specialistJoin.get("averageScore"), maxScore);
                }
                return criteriaBuilder.conjunction();
            };
        }
}
