package com.ezpay.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpiPaymentRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient UPI ID is required")
    private String recipientUpiId;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "1.00", message = "Payment amount must be at least 1.00")
    private BigDecimal amount;

    private String note;

    public UpiPaymentRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecipientUpiId() {
        return recipientUpiId;
    }

    public void setRecipientUpiId(String recipientUpiId) {
        this.recipientUpiId = recipientUpiId;
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
}