package com.quickcommerce.repository;

import com.quickcommerce.entity.OrderTracking;
import com.quickcommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderTrackingRepository extends JpaRepository<OrderTracking, Long> {
    List<OrderTracking> findByOrder(Order order);
    List<OrderTracking> findByOrderOrderByStatusChangedAtDesc(Order order);
}
