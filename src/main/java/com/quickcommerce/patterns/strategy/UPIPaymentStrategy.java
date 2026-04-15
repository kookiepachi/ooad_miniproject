package com.quickcommerce.patterns.strategy;

import java.math.BigDecimal;

public class UPIPaymentStrategy implements PaymentStrategy {
    private String upiId;
    private String transactionPin;

    public UPIPaymentStrategy(String upiId, String transactionPin) {
        this.upiId = upiId;
        this.transactionPin = transactionPin;
    }

    @Override
    public boolean processPayment(String orderId, BigDecimal amount, String reference) {
        System.out.println("[UPI Payment] Processing payment for Order: " + orderId + 
                          " | UPI ID: " + upiId + " | Amount: " + amount);
        // Simulate UPI payment processing
        return validatePaymentDetails() && simulatePaymentGateway(amount);
    }

    @Override
    public boolean validatePaymentDetails() {
        return upiId != null && !upiId.isEmpty() && transactionPin != null && transactionPin.length() == 4;
    }

    @Override
    public String getPaymentMethodName() {
        return "UPI Payment";
    }

    @Override
    public boolean supportsRefund() {
        return true;
    }

    private boolean simulatePaymentGateway(BigDecimal amount) {
        System.out.println("[UPI Gateway] Processing amount: " + amount);
        return true; // Simulate successful payment
    }
}
