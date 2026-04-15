package com.quickcommerce.service;

import com.quickcommerce.dto.DeliveryDTO;
import com.quickcommerce.entity.Delivery;
import com.quickcommerce.entity.Order;
import com.quickcommerce.entity.User;
import com.quickcommerce.enums.OrderStatus;
import com.quickcommerce.repository.DeliveryRepository;
import com.quickcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Delivery Service - Handles order dispatch and tracking
 * Module 3: Delivery & Tracking
 * Features:
 * - Dispatch System: Assigning delivery partner to order and calculating ETA
 * - Live Status Updates: Moving order status from Packing to Out for Delivery
 */
@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    /**
     * Assign delivery partner to order
     * Feature: Dispatch System with automatic delivery partner assignment
     */
    public Delivery assignDeliveryPartner(Order order, Long deliveryPartnerId) {
        User deliveryPartner = userRepository.findById(deliveryPartnerId)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found!"));

        if (!deliveryPartner.getRole().toString().equals("DELIVERY_PARTNER")) {
            throw new RuntimeException("User is not a delivery partner!");
        }

        // Check if order already has delivery
        Delivery existingDelivery = deliveryRepository.findByOrder(order)
                .orElse(null);

        if (existingDelivery != null) {
            throw new RuntimeException("Order already assigned to another delivery partner!");
        }

        // Create delivery assignment
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryPartner(deliveryPartner);
        
        // Set pickup and delivery coordinates (Mock data)
        delivery.setPickupLatitude(28.7041);    // Store location (Delhi)
        delivery.setPickupLongitude(77.1025);
        
        delivery.setDeliveryLatitude(28.6139);   // Customer location (Delhi)
        delivery.setDeliveryLongitude(77.2090);

        // Calculate ETA and distance
        calculateETA(delivery);

        delivery.setDeliveryStatus(OrderStatus.PACKING);
        
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // Update order with delivery partner
        order.setDeliveryPartner(deliveryPartner);
        orderService.updateOrderStatus(order, order.getOrderStatus(), OrderStatus.CONFIRMED);

        return savedDelivery;
    }

    /**
     * Calculate ETA and distance using mock geolocation
     * Feature: ETA Calculation (Simple Haversine formula)
     */
    private void calculateETA(Delivery delivery) {
        // Calculate distance using Haversine formula
        long distanceMeters = calculateDistance(
                delivery.getPickupLatitude(),
                delivery.getPickupLongitude(),
                delivery.getDeliveryLatitude(),
                delivery.getDeliveryLongitude()
        );

        delivery.setEstimatedDistanceMeters(distanceMeters);

        // Average speed: 25 km/h, buffer: 10 mins
        long estimatedSeconds = (distanceMeters / 25000) * 3600 + 600;
        delivery.setEstimatedDeliveryTimeSeconds(estimatedSeconds);

        System.out.println("[DeliveryService] ETA Calculated - Distance: " + (distanceMeters / 1000) + 
                          " km, Time: " + (estimatedSeconds / 60) + " minutes");
    }

    /**
     * Haversine formula to calculate distance between two coordinates
     */
    private long calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int EARTH_RADIUS = 6371; // Radius of Earth in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return (long)(EARTH_RADIUS * c * 1000); // Distance in meters
    }

    /**
     * Update delivery status (Pickup, Out for Delivery, etc.)
     * Feature: Live Status Updates
     */
    public void updateDeliveryStatus(Long deliveryId, OrderStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found!"));

        OrderStatus oldStatus = delivery.getDeliveryStatus();
        delivery.setDeliveryStatus(newStatus);

        // Update corresponding timestamps
        switch(newStatus) {
            case PACKED:
                // Order packed and ready for pickup
                break;
            case OUT_FOR_DELIVERY:
                delivery.setPickedUpAt(LocalDateTime.now());
                break;
            case DELIVERED:
                delivery.setDeliveredAt(LocalDateTime.now());
                delivery.getOrder().setActualDeliveryTime(LocalDateTime.now());
                break;
        }

        deliveryRepository.save(delivery);

        // Update order status
        orderService.updateOrderStatus(delivery.getOrder(), oldStatus, newStatus);
    }

    /**
     * Update delivery partner current location
     * Feature: Live Tracking
     */
    public void updateDeliveryLocation(Long deliveryId, Double latitude, Double longitude) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found!"));

        delivery.setCurrentLatitude(latitude);
        delivery.setCurrentLongitude(longitude);

        // Check if delivery is delayed
        if (LocalDateTime.now().isAfter(
                delivery.getAssignedAt().plusSeconds(delivery.getEstimatedDeliveryTimeSeconds()))) {
            delivery.setIsDelayed(true);
            delivery.setDelayReason("Route congestion");
        }

        deliveryRepository.save(delivery);
        System.out.println("[DeliveryService] Location updated for Delivery ID: " + deliveryId);
    }

    /**
     * Get all deliveries for a delivery partner
     */
    public List<DeliveryDTO> getPartnerDeliveries(Long deliveryPartnerId) {
        User deliveryPartner = userRepository.findById(deliveryPartnerId)
                .orElseThrow(() -> new RuntimeException("Delivery partner not found!"));

        List<Delivery> deliveries = deliveryRepository.findByDeliveryPartner(deliveryPartner);
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active deliveries
     */
    public List<DeliveryDTO> getActiveDeliveries() {
        List<Delivery> deliveries = deliveryRepository
                .findByDeliveryStatus(OrderStatus.OUT_FOR_DELIVERY);
        return deliveries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get delivery by order
     */
    public DeliveryDTO getDeliveryByOrder(Long orderId) {
        // This would need OrderRepository injection - simplified for now
        System.out.println("[DeliveryService] Fetching delivery for Order ID: " + orderId);
        return null;
    }

    /**
     * Convert Delivery entity to DTO
     */
    public DeliveryDTO convertToDTO(Delivery delivery) {
        DeliveryDTO dto = new DeliveryDTO();
        dto.deliveryId = delivery.getDeliveryId();
        return dto;
    }
}
