package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class SpecialistRepositoryImpl extends UserRepositoryImpl<Specialist> implements SpecialistRepository {

    public SpecialistRepositoryImpl() {
        super(Specialist.class);
    }
}
