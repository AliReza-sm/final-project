package ir.maktabsharif.homeserviceprovidersystem.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID extends Serializable> {

    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(T entity);

}
