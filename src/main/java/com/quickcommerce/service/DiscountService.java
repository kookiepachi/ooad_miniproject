package com.quickcommerce.service;

import com.quickcommerce.entity.Discount;
import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.DiscountType;
import com.quickcommerce.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Discount Service - Handles discount calculations and validation
 * Implements Strategy Pattern (OCP Principle) with extensible discount strategies
 */
@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    /**
     * Validate and apply discount to order
     */
    public BigDecimal applyDiscount(Order order, String discountCode) {
        Discount discount = discountRepository.findByDiscountCode(discountCode)
                .orElseThrow(() -> new RuntimeException("Invalid discount code!"));

        // Validate discount
        if (!discount.isValid()) {
            throw new RuntimeException("Discount code is no longer valid!");
        }

        if (order.getSubtotal().compareTo(discount.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Order value does not meet minimum amount for this discount!");
        }

        BigDecimal discountAmount = calculateDiscount(discount, order.getSubtotal());

        // Cap discount to max allowed amount
        if (discount.getMaxDiscountAmount() != null && 
            discountAmount.compareTo(discount.getMaxDiscountAmount()) > 0) {
            discountAmount = discount.getMaxDiscountAmount();
        }

        return discountAmount;
    }

    /**
     * Calculate discount amount based on discount type strategy
     * Demonstrates Open/Closed Principle (OCP) - New discount types can be added without modifying existing code
     */
    private BigDecimal calculateDiscount(Discount discount, BigDecimal orderAmount) {
        switch(discount.getDiscountType()) {
            case PERCENTAGE:
                return orderAmount.multiply(discount.getDiscountValue()).divide(new BigDecimal("100"));
            
            case FIXED_AMOUNT:
                return discount.getDiscountValue();
            
            case FIRST_ORDER:
                return orderAmount.multiply(new BigDecimal("0.10")); // 10% off
            
            case SEASONAL:
                return orderAmount.multiply(discount.getDiscountValue()).divide(new BigDecimal("100"));
            
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Get all active discounts
     */
    public java.util.List<Discount> getActiveDiscounts() {
        return discountRepository.findByIsActive(true);
    }

    /**
     * Create a new discount code (Admin only)
     */
    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    /**
     * Increment usage count
     */
    public void incrementUsageCount(String discountCode) {
        Discount discount = discountRepository.findByDiscountCode(discountCode)
                .orElseThrow(() -> new RuntimeException("Discount not found!"));
        discount.setCurrentUsageCount(discount.getCurrentUsageCount() + 1);
        discountRepository.save(discount);
    }
}
