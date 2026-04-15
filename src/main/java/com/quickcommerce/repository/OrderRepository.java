package com.quickcommerce.repository;

import com.quickcommerce.entity.Order;
import com.quickcommerce.entity.User;
import com.quickcommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
    List<Order> findByCustomerAndOrderStatus(User customer, OrderStatus orderStatus);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    List<Order> findByDeliveryPartner(User deliveryPartner);
    Optional<Order> findByTrackingNumber(String trackingNumber);
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Order> findByPaymentStatusFalse();
    Integer countByCustomer(User customer);
}
