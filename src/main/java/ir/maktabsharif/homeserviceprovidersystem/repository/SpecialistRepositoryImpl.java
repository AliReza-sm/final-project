package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;

public class SpecialistRepositoryImpl extends UserRepositoryImpl<Specialist> implements SpecialistRepository {

    public SpecialistRepositoryImpl() {
        super(Specialist.class);
    }
}
