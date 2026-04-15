package com.quickcommerce.repository;

import com.quickcommerce.entity.WalletTransaction;
import com.quickcommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByUser(User user);
    List<WalletTransaction> findByUserOrderByTransactionDateDesc(User user);
    List<WalletTransaction> findByTransactionType(String transactionType);
    List<WalletTransaction> findByUserAndTransactionDateBetween(User user, LocalDateTime start, LocalDateTime end);
}
