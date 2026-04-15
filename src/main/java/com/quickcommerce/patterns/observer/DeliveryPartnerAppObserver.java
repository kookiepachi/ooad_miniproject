package com.quickcommerce.patterns.observer;

import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.OrderStatus;

public class DeliveryPartnerAppObserver implements OrderObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[DeliveryPartnerApp] Order #" + order.getOrderId() + 
                          " status changed from " + oldStatus + " to " + newStatus);
        if(order.getDeliveryPartner() != null) {
            System.out.println("[DeliveryPartnerApp] Notifying delivery partner: " + 
                              order.getDeliveryPartner().getEmail());
        }
        handleDeliveryTransition(order, newStatus);
    }

    @Override
    public String getObserverName() {
        return "Delivery Partner App";
    }

    private void handleDeliveryTransition(Order order, OrderStatus status) {
        switch(status) {
            case CONFIRMED:
                System.out.println("[DeliveryPartnerApp] New order confirmed. Will be assigned soon.");
                break;
            case PACKED:
                System.out.println("[DeliveryPartnerApp] Order is ready for pickup!");
                break;
            case OUT_FOR_DELIVERY:
                System.out.println("[DeliveryPartnerApp] You are assigned to deliver this order.");
                break;
            case DELIVERED:
                System.out.println("[DeliveryPartnerApp] Delivery completed. Thank you!");
                break;
        }
    }
}
