package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yaml.snakeyaml.events.Event;

import java.io.Serializable;

public interface BaseRepository<T extends BaseEntity<ID>, ID extends Serializable> extends JpaRepository<T, ID> {

}
