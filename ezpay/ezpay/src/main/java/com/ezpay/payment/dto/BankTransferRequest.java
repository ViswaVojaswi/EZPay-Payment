package com.ezpay.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class BankTransferRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Recipient account number is required")
    private String recipientAccountNumber;

    @NotBlank(message = "IFSC code is required")
    @Pattern(
        regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
        message = "Invalid IFSC code"
    )
    private String ifscCode;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "1.00", message = "Transfer amount must be at least 1.00")
    private BigDecimal amount;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    public BankTransferRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}