package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Setter
@Getter

public class Offer extends BaseEntity<Long> {

    @ManyToOne(optional = false)
    private Specialist specialist;

    @ManyToOne(optional = false)
    private Order order;

    @Column
    private LocalDateTime submissionTime;

    @Column
    private Double proposedPrice;

    @Column
    private Integer timeToEndTheJobInHours;

    @Column
    private LocalDateTime proposedStartTime;

    @Enumerated(EnumType.STRING)
    private OfferStatus offerStatus;

    @PrePersist
    public void prePersist(){
        submissionTime = LocalDateTime.now();
    }

}
