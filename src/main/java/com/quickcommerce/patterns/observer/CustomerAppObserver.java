package com.quickcommerce.patterns.observer;

import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.OrderStatus;

public class CustomerAppObserver implements OrderObserver {
    @Override
    public void update(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        System.out.println("[CustomerApp] Order #" + order.getOrderId() + 
                          " status updated from " + oldStatus + " to " + newStatus);
        System.out.println("[CustomerApp] Sending push notification to customer: " + 
                          order.getCustomer().getEmail());
        handleStatusTransition(order, newStatus);
    }

    @Override
    public String getObserverName() {
        return "Customer App";
    }

    private void handleStatusTransition(Order order, OrderStatus newStatus) {
        switch(newStatus) {
            case PENDING:
                System.out.println("[CustomerApp] Order is pending confirmation.");
                break;
            case CONFIRMED:
                System.out.println("[CustomerApp] Order confirmed! Your order will be packed soon.");
                break;
            case PACKING:
                System.out.println("[CustomerApp] Your order is being packed.");
                break;
            case PACKED:
                System.out.println("[CustomerApp] Your order has been packed.");
                break;
            case OUT_FOR_DELIVERY:
                System.out.println("[CustomerApp] Your order is on the way!");
                break;
            case DELIVERED:
                System.out.println("[CustomerApp] Order delivered! Please rate your experience.");
                break;
            case CANCELLED:
                System.out.println("[CustomerApp] Your order has been cancelled.");
                break;
            case RETURNED:
                System.out.println("[CustomerApp] Your order has been returned.");
                break;
        }
    }
}
