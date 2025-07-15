package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Setter
@Getter
public class Payment extends BaseEntity<Long> {

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Column(nullable = false)
    private String captchaCode;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
