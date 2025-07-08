package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public class OrderRepositoryImpl extends CrudRepositoryImpl<Order, Long> implements OrderRepository {

    public OrderRepositoryImpl() {
        super(Order.class);
    }


    @Override
    public List<Order> findByStatus(OrderStatus orderStatus) {
            return entityManager.createQuery("select o from Order o where o.orderStatus = :orderStatus", Order.class)
                    .setParameter("orderStatus", orderStatus)
                    .getResultList();
    }

    @Override
    public List<Order> findByStatusAndService(OrderStatus orderStatus, Set<Service> services) {
        List<Order> orders = new ArrayList<>();
        for (Service service : services) {
            List<Order> resultList = entityManager.createQuery("select o from Order o where o.service = :service and o.orderStatus = :OrderStatus", Order.class)
                    .setParameter("service", service)
                    .setParameter("OrderStatus", orderStatus)
                    .getResultList();
            orders.addAll(resultList);
        }
        return orders;
    }

    @Override
    public boolean existsByServiceId(Long serviceId) {
        try {
            entityManager.createQuery("select o from Order o where o.service.id = :serviceId", Order.class)
                    .setParameter("serviceId", serviceId)
                    .getSingleResult();
            return true;
        }catch (NoResultException e) {
            return false;
        }
    }
}
