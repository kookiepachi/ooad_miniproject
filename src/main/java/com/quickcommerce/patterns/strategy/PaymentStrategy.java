package com.quickcommerce.patterns.strategy;

import java.math.BigDecimal;

/**
 * STRATEGY PATTERN (Behavioral)
 * Defines payment strategies that can be swapped at runtime
 * Supports Single Responsibility Principle (SRP) and Open/Closed Principle (OCP)
 */
public interface PaymentStrategy {
    boolean processPayment(String orderId, BigDecimal amount, String reference);
    boolean validatePaymentDetails();
    String getPaymentMethodName();
    boolean supportsRefund();
}
