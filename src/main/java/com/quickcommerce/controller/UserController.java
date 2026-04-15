package com.quickcommerce.controller;

import com.quickcommerce.dto.LoginRequest;
import com.quickcommerce.dto.LoginResponse;
import com.quickcommerce.dto.RegistrationRequest;
import com.quickcommerce.dto.UserDTO;
import com.quickcommerce.entity.User;
import com.quickcommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * User Controller - RESTful API for user operations
 * Module 4: User & Profile Management
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register new user (Customer, Delivery Partner, Admin)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            return ResponseEntity.ok(userService.convertToDTO(registeredUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Login user and get JWT token
     * Feature: Authentication & RBAC
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    /**
     * Get user profile by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(userService.convertToDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all users (Admin only)
     */
    @GetMapping("/admin/all-users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all delivery partners
     */
    @GetMapping("/delivery-partners")
    public ResponseEntity<?> getAllDeliveryPartners() {
        try {
            List<UserDTO> partners = userService.getAllDeliveryPartners();
            return ResponseEntity.ok(partners);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Update user profile
     */
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(userId, updatedUser);
            return ResponseEntity.ok(userService.convertToDTO(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    /**
     * Verify delivery partner (Admin only)
     */
    @PostMapping("/verify-delivery-partner/{deliveryPartnerId}")
    public ResponseEntity<?> verifyDeliveryPartner(@PathVariable Long deliveryPartnerId) {
        try {
            userService.verifyDeliveryPartner(deliveryPartnerId);
            return ResponseEntity.ok("Delivery partner verified successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
