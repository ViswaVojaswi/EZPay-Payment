package com.ezpay.payment.entity;

import com.ezpay.payment.enums.TransactionStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT_TRANSACTIONS")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "TRANSACTION_TYPE", nullable = false)
    private String transactionType;

    @Column(name = "RECIPIENT_UPI_ID")
    private String recipientUpiId;

    @Column(name = "RECIPIENT_ACCOUNT_NUMBER")
    private String recipientAccountNumber;

    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "PURPOSE")
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TransactionStatus status;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    public PaymentTransaction() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRecipientUpiId() {
        return recipientUpiId;
    }

    public void setRecipientUpiId(String recipientUpiId) {
        this.recipientUpiId = recipientUpiId;
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}