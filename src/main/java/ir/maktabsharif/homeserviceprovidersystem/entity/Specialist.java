package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Specialists")
@PrimaryKeyJoinColumn(name = "user_id")

public class Specialist extends User {

    @Column(nullable = false)
    private String profilePhoto;

    @Enumerated(EnumType.STRING)
    private SpecialistStatus specialistStatus;

    @ManyToMany
    @JoinTable(name = "specialist_service",
            joinColumns = @JoinColumn(name = "specialist_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> specialistServices;

    @Column
    private Proposal proposal;
}
