package ir.maktabsharif.homeserviceprovidersystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TemporaryEmail extends BaseEntity<Long>{

    @OneToOne(optional = false)
    private User user;

    @Column(nullable = false)
    @Email(message = "email format is wrong")
    private String email;

}
