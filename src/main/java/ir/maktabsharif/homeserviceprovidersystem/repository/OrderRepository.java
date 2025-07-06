package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByStatus(OrderStatus orderStatus);
    List<Order> findByStatusAndService(OrderStatus orderStatus, Set<Service> services);
    boolean existsByServiceId(Long serviceId);

}
