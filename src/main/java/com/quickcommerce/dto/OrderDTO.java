package com.quickcommerce.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderDTO {
    public Long orderId;
    public Long userId;
    public BigDecimal totalAmount;
    public BigDecimal estimatedDeliveryFee;
    public String orderStatus;
    public String paymentMethod;
    public List<Object> items;
    public String deliveryAddress;
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
    public List<Object> getItems() { return items; }
    public void setItems(List<Object> items) { this.items = items; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public Long getDeliveryPartnerId() { return deliveryPartnerId; }
    public void setDeliveryPartnerId(Long deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public BigDecimal getEstimatedDeliveryFee() { return estimatedDeliveryFee; }
    public void setEstimatedDeliveryFee(BigDecimal estimatedDeliveryFee) { this.estimatedDeliveryFee = estimatedDeliveryFee; }
}

