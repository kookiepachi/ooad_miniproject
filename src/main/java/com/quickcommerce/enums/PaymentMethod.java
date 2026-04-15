package com.quickcommerce.enums;

public enum PaymentMethod {
    UPI("UPI Payment"),
    CARD("Card Payment"),
    COD("Cash on Delivery"),
    WALLET("Wallet Payment");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
