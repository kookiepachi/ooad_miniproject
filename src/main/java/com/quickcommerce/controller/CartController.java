package com.quickcommerce.controller;

import com.quickcommerce.entity.Cart;
import com.quickcommerce.entity.User;
import com.quickcommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * Cart Controller - RESTful API for cart operations
 * Module 2: Order & Cart Management
 */
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * Get user cart
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            // In real app, get user from authentication context
            User mockUser = new User();
            mockUser.setUserId(userId);
            Cart cart = cartService.getOrCreateCart(mockUser);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Add item to cart with stock validation
     * Feature: Cart logic with only available products
     */
    @PostMapping("/{userId}/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            cartService.addToCart(mockUser, productId, quantity);
            return ResponseEntity.ok("Item added to cart");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Remove item from cart
     */
    @DeleteMapping("/{userId}/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            cartService.removeFromCart(mockUser, productId);
            return ResponseEntity.ok("Item removed from cart");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Update cart item quantity
     */
    @PutMapping("/{userId}/update/{productId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam Integer newQuantity) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            cartService.updateCartItemQuantity(mockUser, productId, newQuantity);
            return ResponseEntity.ok("Quantity updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Clear cart
     */
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<?> clearCart(@PathVariable Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            cartService.clearCart(mockUser);
            return ResponseEntity.ok("Cart cleared");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get cart total
     */
    @GetMapping("/{userId}/total")
    public ResponseEntity<?> getCartTotal(@PathVariable Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            BigDecimal total = cartService.getCartTotal(mockUser);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
