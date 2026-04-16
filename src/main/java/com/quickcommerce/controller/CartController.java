package com.quickcommerce.controller;

import com.quickcommerce.entity.Cart;
import com.quickcommerce.entity.User;
import com.quickcommerce.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Get user cart
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            User user = getUserOrThrow(userId);
            Cart cart = cartService.getOrCreateCart(user);
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
            @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            User user = getUserOrThrow(userId);
            cartService.addToCart(user, productId, quantity);
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
            User user = getUserOrThrow(userId);
            cartService.removeFromCart(user, productId);
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
            User user = getUserOrThrow(userId);
            cartService.updateCartItemQuantity(user, productId, newQuantity);
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
            User user = getUserOrThrow(userId);
            cartService.clearCart(user);
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
            User user = getUserOrThrow(userId);
            BigDecimal total = cartService.getCartTotal(user);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
