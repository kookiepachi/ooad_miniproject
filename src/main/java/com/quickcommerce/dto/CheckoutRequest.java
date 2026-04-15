package com.quickcommerce.dto;

import java.math.BigDecimal;

public class CheckoutRequest {
    public Long cartId;
    public String paymentMethod;
    public String deliveryAddress;
    public String deliveryInstructions;
    public String deliveryCity;
    public String deliveryZipCode;
    public String specialInstructions;
    public BigDecimal appliedDiscount = BigDecimal.ZERO;
    public String discountCode;
    
    public CheckoutRequest() {}
    public CheckoutRequest(Long cartId, String paymentMethod, String deliveryAddress) {
        this.cartId = cartId;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
    }
    
    public Long getCartId() { return cartId; }
    public void setCartId(Long cartId) { this.cartId = cartId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String deliveryInstructions) { this.deliveryInstructions = deliveryInstructions; }
    public BigDecimal getAppliedDiscount() { return appliedDiscount; }
    public void setAppliedDiscount(BigDecimal appliedDiscount) { this.appliedDiscount = appliedDiscount; }
    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }
    public String getDeliveryCity() { return deliveryCity; }
    public void setDeliveryCity(String deliveryCity) { this.deliveryCity = deliveryCity; }
    public String getDeliveryZipCode() { return deliveryZipCode; }
    public void setDeliveryZipCode(String deliveryZipCode) { this.deliveryZipCode = deliveryZipCode; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}

