package com.ezpay.payment.dto;

import com.ezpay.payment.enums.TransactionStatus;

import java.math.BigDecimal;

public class PaymentResponse {

    private Long transactionId;
    private String message;
    private TransactionStatus status;
    private BigDecimal amount;

    public PaymentResponse() {
    }

    public PaymentResponse(
            Long transactionId,
            String message,
            TransactionStatus status,
            BigDecimal amount) {

        this.transactionId = transactionId;
        this.message = message;
        this.status = status;
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getMessage() {
        return message;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}