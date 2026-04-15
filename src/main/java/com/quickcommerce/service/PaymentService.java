package com.quickcommerce.service;

import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.PaymentMethod;
import com.quickcommerce.patterns.strategy.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Payment Service - Processes payments using Strategy Pattern
 * Demonstrates: Strategy Pattern (Behavioral), Dependency Inversion Principle (DIP)
 */
@Service
public class PaymentService {

    /**
     * Process payment with selected payment strategy
     * Strategy pattern: Payment strategy can be switched at runtime
     */
    public boolean processPayment(Order order, PaymentMethod paymentMethod, String... paymentDetails) {
        PaymentStrategy paymentStrategy = getPaymentStrategy(paymentMethod, paymentDetails);

        if (!paymentStrategy.validatePaymentDetails()) {
            throw new RuntimeException("Invalid " + paymentStrategy.getPaymentMethodName() + " details!");
        }

        boolean success = paymentStrategy.processPayment(
                order.getOrderId().toString(),
                order.getTotalAmount(),
                order.getTrackingNumber()
        );

        if (success) {
            order.setPaymentStatus(true);
            System.out.println("[PaymentService] Payment processed successfully via " + 
                             paymentStrategy.getPaymentMethodName());
        }

        return success;
    }

    /**
     * Get appropriate payment strategy based on payment method
     * Strategy selection logic - demonstrates DIP (Dependency Inversion Principle)
     */
    private PaymentStrategy getPaymentStrategy(PaymentMethod method, String... details) {
        switch(method) {
            case UPI:
                // details: [upiId, transactionPin]
                return new UPIPaymentStrategy(details[0], details[1]);

            case CARD:
                // details: [cardNumber, cardHolderName, cvv, expiryDate]
                return new CardPaymentStrategy(details[0], details[1], details[2], details[3]);

            case COD:
                // details: [deliveryAddress]
                return new CODPaymentStrategy(details[0]);

            case WALLET:
                return new WalletPaymentStrategy();

            default:
                throw new IllegalArgumentException("Unknown payment method: " + method);
        }
    }

    /**
     * Check if payment method supports refunds
     */
    public boolean supportsRefund(PaymentMethod method) {
        PaymentStrategy strategy = getPaymentStrategy(method);
        return strategy.supportsRefund();
    }
}
