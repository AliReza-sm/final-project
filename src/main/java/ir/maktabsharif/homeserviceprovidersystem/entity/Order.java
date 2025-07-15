package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter

public class Order extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "customer can not be null")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    @NotNull(message = "service can not be null")
    private Service service;

    @Column
    private Double proposedPrice;

    @Column
    private String description;

    @Column
    private String address;

    @Column
    private LocalDateTime proposedStartDate;

    @Column
    private LocalDateTime orderCreatedDate;

    @Column
    private LocalDateTime workCompletedDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<Offer> offers = new ArrayList<>();

    @OneToOne
    private Offer selectedOffer;

    @PrePersist
    public void prePersist() {
        orderCreatedDate = LocalDateTime.now();
    }
}
