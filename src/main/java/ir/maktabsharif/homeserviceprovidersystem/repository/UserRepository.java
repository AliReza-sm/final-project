package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository <T extends User> extends BaseRepository<T, Long>, JpaSpecificationExecutor<T> {

    Optional<T> findByEmail(String email);

}
