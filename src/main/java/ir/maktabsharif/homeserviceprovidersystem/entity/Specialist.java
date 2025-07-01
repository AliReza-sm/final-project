package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Specialists")
@PrimaryKeyJoinColumn(name = "user_id")

public class Specialist extends User {

    @Column
    private String profilePhotoPath;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePhotoBytes;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private SpecialistStatus specialistStatus;

    @Column
    private Double sumScore = 0D;

    @Column
    private Integer numberOfReviews = 0;

    @Column
    private Double averageScore = 0D;

    @ManyToMany
    @JoinTable(name = "specialist_service",
            joinColumns = @JoinColumn(name = "specialist_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> specialistServices = new HashSet<>();

}
