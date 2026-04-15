package com.quickcommerce.controller;

import com.quickcommerce.dto.DeliveryDTO;
import com.quickcommerce.entity.Delivery;
import com.quickcommerce.entity.Order;
import com.quickcommerce.enums.OrderStatus;
import com.quickcommerce.service.DeliveryService;
import com.quickcommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Delivery Controller - RESTful API for delivery operations
 * Module 3: Delivery & Tracking
 * Features: Dispatch System (ETA calculation), Live Status Updates
 */
@RestController
@RequestMapping("/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private OrderService orderService;

    /**
     * Assign delivery partner to order
     * Feature: Dispatch System with automatic delivery partner assignment and ETA calculation
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignDeliveryPartner(
            @RequestParam Long orderId,
            @RequestParam Long deliveryPartnerId) {
        try {
            Order order = orderService.getOrderById(orderId);
            Delivery delivery = deliveryService.assignDeliveryPartner(order, deliveryPartnerId);
            return ResponseEntity.ok("Delivery partner assigned. ETA: " + 
                                    (delivery.getEstimatedDeliveryTimeSeconds() / 60) + " minutes");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Update delivery status (Packed, Out for Delivery, Delivered)
     * Feature: Live Status Updates - Moving order status from Packing to Out for Delivery
     */
    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<?> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestParam OrderStatus newStatus) {
        try {
            deliveryService.updateDeliveryStatus(deliveryId, newStatus);
            return ResponseEntity.ok("Delivery status updated to: " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Update delivery partner's current location
     * Feature: Live Tracking - Real-time location updates
     */
    @PutMapping("/{deliveryId}/location")
    public ResponseEntity<?> updateDeliveryLocation(
            @PathVariable Long deliveryId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            deliveryService.updateDeliveryLocation(deliveryId, latitude, longitude);
            return ResponseEntity.ok("Location updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all deliveries for a delivery partner
     */
    @GetMapping("/partner/{deliveryPartnerId}")
    public ResponseEntity<?> getPartnerDeliveries(@PathVariable Long deliveryPartnerId) {
        try {
            List<DeliveryDTO> deliveries = deliveryService.getPartnerDeliveries(deliveryPartnerId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all active deliveries
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveDeliveries() {
        try {
            List<DeliveryDTO> deliveries = deliveryService.getActiveDeliveries();
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
