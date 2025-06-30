package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "services")
@Setter
@Getter

public class Service extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    @NotBlank(message = "service name can not be blank")
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @Positive(message = "base price must be positive")
    private Double basePrice;

    @ManyToOne
    @JoinColumn(name = "parent_service_id")
    private Service parentService;

    @OneToMany(mappedBy = "parentService", cascade = CascadeType.ALL)
    private List<Service> subServices = new ArrayList<>();

    @ManyToMany(mappedBy = "specialistServices")
    private Set<Specialist> qualifiedSpecialists = new HashSet<>();
}
