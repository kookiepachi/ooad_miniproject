package com.quickcommerce.controller;

import com.quickcommerce.entity.User;
import com.quickcommerce.entity.WalletTransaction;
import com.quickcommerce.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Wallet Controller - RESTful API for wallet operations
 * Module 4: Wallet & Payments (Minor Feature)
 */
@RestController
@RequestMapping("/wallet")
@CrossOrigin(origins = "*")
public class WalletController {

    @Autowired
    private WalletService walletService;

    /**
     * Add money to wallet
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addToWallet(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            mockUser.setWalletBalance(BigDecimal.ZERO);
            walletService.addToWallet(mockUser, amount, description);
            return ResponseEntity.ok("Amount added to wallet successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get wallet balance
     */
    @GetMapping("/{userId}/balance")
    public ResponseEntity<?> getWalletBalance(@PathVariable Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            mockUser.setWalletBalance(BigDecimal.ZERO);
            BigDecimal balance = walletService.getWalletBalance(mockUser);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get wallet transaction history
     */
    @GetMapping("/{userId}/transactions")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long userId) {
        try {
            User mockUser = new User();
            mockUser.setUserId(userId);
            List<WalletTransaction> transactions = walletService.getTransactionHistory(mockUser);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
