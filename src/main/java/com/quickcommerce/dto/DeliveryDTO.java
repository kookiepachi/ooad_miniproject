package com.quickcommerce.dto;

import java.math.BigDecimal;

public class DeliveryDTO {
    public Long deliveryId;
    public Long orderId;
    public Long deliveryPartnerId;
    public String deliveryStatus;
    public String currentLocation;
    public String estimatedTime;
    public BigDecimal fee;
    
    public DeliveryDTO() {}
    public DeliveryDTO(Long deliveryId, Long orderId, String deliveryStatus) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.deliveryStatus = deliveryStatus;
    }
    
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getDeliveryPartnerId() { return deliveryPartnerId; }
    public void setDeliveryPartnerId(Long deliveryPartnerId) { this.deliveryPartnerId = deliveryPartnerId; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public String getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }
    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }
}

