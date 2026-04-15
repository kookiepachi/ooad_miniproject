package com.quickcommerce.service;

import com.quickcommerce.entity.User;
import com.quickcommerce.entity.WalletTransaction;
import com.quickcommerce.repository.UserRepository;
import com.quickcommerce.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

/**
 * Wallet Service - Manages user wallet and payment transactions
 * Implements SRP (Single Responsibility Principle) - Handles only wallet operations
 */
@Service
public class WalletService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    /**
     * Add money to wallet
     */
    public void addToWallet(User user, BigDecimal amount, String description) {
        user.setWalletBalance(user.getWalletBalance().add(amount));
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUser(user);
        transaction.setTransactionType("CREDIT");
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionStatus("COMPLETED");

        walletTransactionRepository.save(transaction);
    }

    /**
     * Deduct money from wallet
     */
    public boolean deductFromWallet(User user, BigDecimal amount, String description) {
        if (user.getWalletBalance().compareTo(amount) < 0) {
            return false; // Insufficient balance
        }

        user.setWalletBalance(user.getWalletBalance().subtract(amount));
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUser(user);
        transaction.setTransactionType("DEBIT");
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionStatus("COMPLETED");

        walletTransactionRepository.save(transaction);
        return true;
    }

    /**
     * Get wallet transactions for a user
     */
    public List<WalletTransaction> getTransactionHistory(User user) {
        return walletTransactionRepository.findByUserOrderByTransactionDateDesc(user);
    }

    /**
     * Get wallet balance
     */
    public BigDecimal getWalletBalance(User user) {
        return user.getWalletBalance();
    }
}
