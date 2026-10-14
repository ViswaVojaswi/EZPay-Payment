package com.ezpay.payment.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PAYMENT_ACCOUNTS")
public class PaymentAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false, unique = true)
    private Long userId;

    @Column(name = "UPI_ID", unique = true)
    private String upiId;

    @Column(name = "BALANCE", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public PaymentAccount() {
    }

    public PaymentAccount(Long userId, String upiId, BigDecimal balance) {
        this.userId = userId;
        this.upiId = upiId;
        this.balance = balance;
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

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}