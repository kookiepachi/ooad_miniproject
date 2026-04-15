package com.quickcommerce.patterns.factory;

import com.quickcommerce.entity.User;
import com.quickcommerce.enums.UserRole;

/**
 * FACTORY METHOD PATTERN (Creational)
 * This pattern provides an interface for creating different types of users
 * without specifying the exact classes, adhering to the Open/Closed Principle (OCP)
 */
public abstract class UserFactory {
    public static User createUser(UserRole role, String email, String password, String fullName, String phoneNumber) {
        switch(role) {
            case CUSTOMER:
                return createCustomer(email, password, fullName, phoneNumber);
            case DELIVERY_PARTNER:
                return createDeliveryPartner(email, password, fullName, phoneNumber);
            case ADMIN:
                return createAdmin(email, password, fullName, phoneNumber);
            case STORE_MANAGER:
                return createStoreManager(email, password, fullName, phoneNumber);
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }

    private static User createCustomer(String email, String password, String fullName, String phoneNumber) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.CUSTOMER);
        user.setIsActive(true);
        user.setTotalOrders(0);
        return user;
    }

    private static User createDeliveryPartner(String email, String password, String fullName, String phoneNumber) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.DELIVERY_PARTNER);
        user.setIsActive(false); // Needs verification
        user.setTotalDeliveries(0);
        user.setDeliveryRating(0.0);
        return user;
    }

    private static User createAdmin(String email, String password, String fullName, String phoneNumber) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.ADMIN);
        user.setIsActive(true);
        user.setIsVerified(true);
        return user;
    }

    private static User createStoreManager(String email, String password, String fullName, String phoneNumber) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(UserRole.STORE_MANAGER);
        user.setIsActive(true);
        return user;
    }
}
