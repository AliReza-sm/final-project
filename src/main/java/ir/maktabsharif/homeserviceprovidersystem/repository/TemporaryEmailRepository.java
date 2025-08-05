package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.TemporaryEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryEmailRepository extends BaseRepository<TemporaryEmail, Long> {

    Optional<TemporaryEmail> findByUserId(Long userId);
}
