package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Setter
@Getter
public class Wallet extends BaseEntity<Long> {

    @OneToOne
    private User user;

    @Column
    private Double balance = 0d;


}
