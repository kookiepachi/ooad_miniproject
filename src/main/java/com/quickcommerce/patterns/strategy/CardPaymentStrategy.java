package com.quickcommerce.patterns.strategy;

import java.math.BigDecimal;

public class CardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;

    public CardPaymentStrategy(String cardNumber, String cardHolderName, String cvv, String expiryDate) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean processPayment(String orderId, BigDecimal amount, String reference) {
        System.out.println("[Card Payment] Processing payment for Order: " + orderId + 
                          " | Card: ****" + cardNumber.substring(cardNumber.length() - 4) + 
                          " | Amount: " + amount);
        return validatePaymentDetails() && simulatePaymentGateway(amount);
    }

    @Override
    public boolean validatePaymentDetails() {
        return cardNumber != null && cardNumber.length() == 16 &&
               cvv != null && cvv.length() == 3 &&
               cardHolderName != null && !cardHolderName.isEmpty();
    }

    @Override
    public String getPaymentMethodName() {
        return "Card Payment";
    }

    @Override
    public boolean supportsRefund() {
        return true;
    }

    private boolean simulatePaymentGateway(BigDecimal amount) {
        System.out.println("[Card Gateway] Processing amount: " + amount);
        return true; // Simulate successful payment
    }
}
