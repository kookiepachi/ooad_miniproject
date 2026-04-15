package com.quickcommerce.patterns.observer;

import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.OrderStatus;

/**
 * OBSERVER PATTERN (Behavioral)
 * When order status changes, all observers (Customer app, Delivery partner app) are notified
 * This implements the Dependency Inversion Principle (DIP)
 */
public interface OrderObserver {
    void update(Order order, OrderStatus oldStatus, OrderStatus newStatus);
    String getObserverName();
}
