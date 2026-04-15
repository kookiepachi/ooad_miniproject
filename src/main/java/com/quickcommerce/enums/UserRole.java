package com.quickcommerce.enums;

public enum UserRole {
    CUSTOMER("Customer"),
    DELIVERY_PARTNER("Delivery Partner"),
    ADMIN("Admin"),
    STORE_MANAGER("Store Manager");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
