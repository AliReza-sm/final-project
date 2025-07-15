package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")

public class User extends BaseEntity<Long>{

    @Column(nullable = false)
    @NotBlank(message = "firstname can not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
    private String firstname;

    @Column(nullable = false)
    @NotBlank(message = "lastname can not be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "firstname must contain only alphabetic characters")
    private String lastname;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "email can not be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
            message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "password can not be blank")
    @Pattern(regexp = "^[A-Za-z0-9]{8,}$", message = "password must be at least 8 character or number")
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @NotNull(message = "wallet can not be null")
    private Wallet wallet;

    @Column
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    public void prePersist(){
        registrationDate = LocalDateTime.now();
    }
}
