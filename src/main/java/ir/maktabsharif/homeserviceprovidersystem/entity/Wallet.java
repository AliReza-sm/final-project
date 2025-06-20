package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
@Setter
@Getter
public class Wallet extends BaseEntity<Long> {

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

}
