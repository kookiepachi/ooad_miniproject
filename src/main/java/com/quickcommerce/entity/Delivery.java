package com.quickcommerce.entity;

import com.quickcommerce.enums.OrderStatus;
import jakarta.persistence.*;
// lombok removed
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")




public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_partner_id", nullable = false)
    private User deliveryPartner;

    @Column(nullable = false)
    private Double pickupLatitude;

    @Column(nullable = false)
    private Double pickupLongitude;

    @Column(nullable = false)
    private Double deliveryLatitude;

    @Column(nullable = false)
    private Double deliveryLongitude;

    private Double currentLatitude;

    private Double currentLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus deliveryStatus = OrderStatus.PACKING;

    private Long estimatedDeliveryTimeSeconds;

    private Long estimatedDistanceMeters;

    @Column(nullable = false, updatable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    private LocalDateTime pickedUpAt;

    private LocalDateTime deliveredAt;

    private String deliveryNotes;

    private String deliveryProofImageUrl;

    private Boolean requiresSignature = false;

    private Boolean isDelayed = false;

    private String delayReason;

    // Getters and Setters
    public Long getDeliveryId() { return deliveryId; }
    public void setDeliveryId(Long deliveryId) { this.deliveryId = deliveryId; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public User getDeliveryPartner() { return deliveryPartner; }
    public void setDeliveryPartner(User deliveryPartner) { this.deliveryPartner = deliveryPartner; }
    public Double getPickupLatitude() { return pickupLatitude; }
    public void setPickupLatitude(Double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
    public Double getPickupLongitude() { return pickupLongitude; }
    public void setPickupLongitude(Double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
    public Double getDeliveryLatitude() { return deliveryLatitude; }
    public void setDeliveryLatitude(Double deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }
    public Double getDeliveryLongitude() { return deliveryLongitude; }
    public void setDeliveryLongitude(Double deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }
    public Double getCurrentLatitude() { return currentLatitude; }
    public void setCurrentLatitude(Double currentLatitude) { this.currentLatitude = currentLatitude; }
    public Double getCurrentLongitude() { return currentLongitude; }
    public void setCurrentLongitude(Double currentLongitude) { this.currentLongitude = currentLongitude; }
    public OrderStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(OrderStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }
    public Long getEstimatedDeliveryTimeSeconds() { return estimatedDeliveryTimeSeconds; }
    public void setEstimatedDeliveryTimeSeconds(Long estimatedDeliveryTimeSeconds) { this.estimatedDeliveryTimeSeconds = estimatedDeliveryTimeSeconds; }
    public Long getEstimatedDistanceMeters() { return estimatedDistanceMeters; }
    public void setEstimatedDistanceMeters(Long estimatedDistanceMeters) { this.estimatedDistanceMeters = estimatedDistanceMeters; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public LocalDateTime getPickedUpAt() { return pickedUpAt; }
    public void setPickedUpAt(LocalDateTime pickedUpAt) { this.pickedUpAt = pickedUpAt; }
    public LocalDateTime getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(LocalDateTime deliveredAt) { this.deliveredAt = deliveredAt; }
    public String getDeliveryNotes() { return deliveryNotes; }
    public void setDeliveryNotes(String deliveryNotes) { this.deliveryNotes = deliveryNotes; }
    public String getDeliveryProofImageUrl() { return deliveryProofImageUrl; }
    public void setDeliveryProofImageUrl(String deliveryProofImageUrl) { this.deliveryProofImageUrl = deliveryProofImageUrl; }
    public Boolean getRequiresSignature() { return requiresSignature; }
    public void setRequiresSignature(Boolean requiresSignature) { this.requiresSignature = requiresSignature; }
    public Boolean getIsDelayed() { return isDelayed; }
    public void setIsDelayed(Boolean isDelayed) { this.isDelayed = isDelayed; }
    public String getDelayReason() { return delayReason; }
    public void setDelayReason(String delayReason) { this.delayReason = delayReason; }
}
