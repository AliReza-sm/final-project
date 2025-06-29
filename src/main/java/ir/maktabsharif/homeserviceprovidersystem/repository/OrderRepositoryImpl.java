package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Offer;
import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

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
}
