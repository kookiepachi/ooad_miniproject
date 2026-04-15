package com.quickcommerce.controller;

import com.quickcommerce.dto.CheckoutRequest;
import com.quickcommerce.dto.OrderDTO;
import com.quickcommerce.entity.Order;
import com.quickcommerce.entity.OrderTracking;
import com.quickcommerce.entity.User;
import com.quickcommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Order Controller - RESTful API for order operations
 * Module 2: Order & Cart Management
 * Features: Order Processing, Checkout Flow, Order History, Re-ordering
 */
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Create order from cart
     * Feature: Order Processing with price calculation (discounts, taxes, delivery charges)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(
            @RequestParam Long userId,
            @RequestBody CheckoutRequest checkoutRequest) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            Order order = orderService.createOrderFromCart(mockUser, checkoutRequest);
            return ResponseEntity.ok("Order created successfully. Tracking: " + order.getTrackingNumber());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get order by ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(orderService.convertToDTO(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get order history for customer
     * Feature: Order History and re-ordering functionality
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            List<OrderDTO> orders = orderService.getOrderHistory(mockUser);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get order tracking history
     * Feature: Live Status Updates - Track order status changes
     */
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<?> getOrderTracking(@PathVariable Long orderId) {
        try {
            List<OrderTracking> tracking = orderService.getOrderTrackingHistory(orderId);
            return ResponseEntity.ok(tracking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get pending orders (Admin/Store Manager)
     */
    @GetMapping("/admin/pending")
    public ResponseEntity<?> getPendingOrders() {
        try {
            List<OrderDTO> orders = orderService.getPendingOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Reorder from previous order
     * Feature: Re-ordering functionality
     */
    @PostMapping("/reorder/{previousOrderId}")
    public ResponseEntity<?> reorder(
            @PathVariable Long previousOrderId,
            @RequestParam Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            Order reorder = orderService.reorderFromPreviousOrder(mockUser, previousOrderId);
            return ResponseEntity.ok("Reorder created. New Tracking: " + reorder.getTrackingNumber());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Process payment for order
     */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<?> processPayment(
            @PathVariable Long orderId,
            @RequestParam String paymentMethod,
            @RequestBody String[] paymentDetails) {
        try {
            orderService.processOrderPayment(orderId, paymentDetails);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
