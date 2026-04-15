package com.quickcommerce.patterns.observer;

import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.OrderStatus;
import java.util.ArrayList;
import java.util.List;

public class OrderSubject {
    private List<OrderObserver> observers = new ArrayList<>();

    public void attachObserver(OrderObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[OrderSubject] Observer attached: " + observer.getObserverName());
        }
    }

    public void detachObserver(OrderObserver observer) {
        if (observers.contains(observer)) {
            observers.remove(observer);
            System.out.println("[OrderSubject] Observer detached: " + observer.getObserverName());
        }
    }

    public void notifyObservers(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("\n[OrderSubject] Notifying all observers about status change...");
        for (OrderObserver observer : observers) {
            observer.update(order, oldStatus, newStatus);
        }
    }

    public List<OrderObserver> getObservers() {
        return new ArrayList<>(observers);
    }
}
