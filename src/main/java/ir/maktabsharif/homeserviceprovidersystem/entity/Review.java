package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Setter
@Getter

public class Review extends BaseEntity<Long> {

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Specialist specialist;

    @OneToOne(optional = false)
    private Order order;

    @Column(nullable = false)
    private Integer rating;

    @Column
    private String comment;
}
