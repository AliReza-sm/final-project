package ir.maktabsharif.homeserviceprovidersystem.repository;

import ir.maktabsharif.homeserviceprovidersystem.entity.Order;
import ir.maktabsharif.homeserviceprovidersystem.entity.OrderStatus;
import ir.maktabsharif.homeserviceprovidersystem.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select case when count (o) > 0 then true else false end from Order o where o.service.id = :serviceId")
    boolean existsByServiceId(Long serviceId);
    List<Order> findByOrderStatusInAndServiceIn(List<OrderStatus> statuses, Set<Service> services);
    Page<Order> findAllByCustomerId(Long customerId, Pageable pageable);
    Page<Order> findAllByCustomerIdAndOrderStatus(Long customerId, OrderStatus status, Pageable pageable);
}
