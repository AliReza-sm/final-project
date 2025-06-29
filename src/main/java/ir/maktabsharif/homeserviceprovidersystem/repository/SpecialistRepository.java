package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Customer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Specialist;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistRepository extends UserRepository<Specialist> {

}
