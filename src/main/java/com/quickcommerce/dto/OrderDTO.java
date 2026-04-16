package com.quickcommerce.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderDTO {
    public Long orderId;
    public Long userId;
    public BigDecimal subtotal;
    public BigDecimal discountAmount;
    public BigDecimal deliveryCharge;
    public BigDecimal totalAmount;
    public BigDecimal estimatedDeliveryFee;
    public String orderStatus;
    public String paymentMethod;
    public List<Map<String, Object>> items;
    public String deliveryAddress;
    public String deliveryCity;
    public String deliveryZipCode;
    public String trackingNumber;
    public String createdAt;
    public String specialInstructions;
    public Long deliveryPartnerId;
    
    public OrderDTO() {}
    public OrderDTO(Long orderId, Long userId, BigDecimal totalAmount, String orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
    }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public List<Map<String, Object>> getItems() { return items; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public String getDeliveryCity() { return deliveryCity; }
    public void setDeliveryCity(String deliveryCity) { this.deliveryCity = deliveryCity; }
    public String getDeliveryZipCode() { return deliveryZipCode; }
    public void setDeliveryZipCode(String deliveryZipCode) { this.deliveryZipCode = deliveryZipCode; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    public Long getDeliveryPartnerId() { return deliveryPartnerId; }
    public void setDeliveryPartnerId(Long deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public BigDecimal getEstimatedDeliveryFee() { return estimatedDeliveryFee; }
    public void setEstimatedDeliveryFee(BigDecimal estimatedDeliveryFee) { this.estimatedDeliveryFee = estimatedDeliveryFee; }
}

