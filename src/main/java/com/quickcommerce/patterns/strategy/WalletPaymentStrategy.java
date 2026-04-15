package com.quickcommerce.patterns.strategy;

import java.math.BigDecimal;

public class WalletPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(String orderId, BigDecimal amount, String reference) {
        System.out.println("[Wallet Payment] Processing payment for Order: " + orderId + 
                          " | Amount: " + amount + " | Reference: " + reference);
        return true; // Wallet payment handled separately by WalletService
    }

    @Override
    public boolean validatePaymentDetails() {
        return true; // Wallet validation done in WalletService
    }

    @Override
    public String getPaymentMethodName() {
        return "Wallet Payment";
    }

    @Override
    public boolean supportsRefund() {
        return true;
    }
}
