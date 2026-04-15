package com.quickcommerce.repository;

import com.quickcommerce.entity.Delivery;
import com.quickcommerce.entity.Order;
import com.quickcommerce.entity.User;
import com.quickcommerce.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrder(Order order);
    List<Delivery> findByDeliveryPartner(User deliveryPartner);
    List<Delivery> findByDeliveryStatus(OrderStatus deliveryStatus);
    List<Delivery> findByDeliveryPartnerAndDeliveryStatus(User deliveryPartner, OrderStatus status);
    Integer countByDeliveryPartner(User deliveryPartner);
}
