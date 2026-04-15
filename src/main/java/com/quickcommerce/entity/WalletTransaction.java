package com.quickcommerce.entity;

import jakarta.persistence.*;
// lombok removed
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_transactions")




public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String transactionType; // DEBIT, CREDIT

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    private String referenceOrderId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    private String transactionStatus = "COMPLETED"; // PENDING, COMPLETED, FAILED

    private String paymentGatewayTransactionId;

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReferenceOrderId() { return referenceOrderId; }
    public void setReferenceOrderId(String referenceOrderId) { this.referenceOrderId = referenceOrderId; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getTransactionStatus() { return transactionStatus; }
    public void setTransactionStatus(String transactionStatus) { this.transactionStatus = transactionStatus; }
    public String getPaymentGatewayTransactionId() { return paymentGatewayTransactionId; }
    public void setPaymentGatewayTransactionId(String paymentGatewayTransactionId) { this.paymentGatewayTransactionId = paymentGatewayTransactionId; }
}
