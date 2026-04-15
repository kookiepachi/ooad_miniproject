package com.quickcommerce.patterns.strategy;

import java.math.BigDecimal;

public class CODPaymentStrategy implements PaymentStrategy {
    private String deliveryAddress;

    public CODPaymentStrategy(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public boolean processPayment(String orderId, BigDecimal amount, String reference) {
        System.out.println("[COD Payment] Order: " + orderId + 
                          " | Delivery Address: " + deliveryAddress + 
                          " | Amount to be collected: " + amount);
        return true; // COD is always accepted initially
    }

    @Override
    public boolean validatePaymentDetails() {
        return deliveryAddress != null && !deliveryAddress.isEmpty();
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash on Delivery";
    }

    @Override
    public boolean supportsRefund() {
        return false; // COD doesn't support refunds easily
    }
}
