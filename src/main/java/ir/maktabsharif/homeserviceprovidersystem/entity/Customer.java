package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "customers")
@PrimaryKeyJoinColumn(name = "user_id")
@Setter
@Getter

public class Customer extends User {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Review> reviews;
}
