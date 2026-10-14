package com.ezpay.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ezpay.payment.exception.PaymentException;

class BankTransferServiceTest {

    private BankTransferService bankTransferService;

    @BeforeEach
    void setUp() {
        bankTransferService = new BankTransferService();
    }

    @Test
    void testProcessTransfer_ValidDetails() {

        boolean result = bankTransferService.processTransfer(
                "123456789012",
                "SBIN0001234",
                new BigDecimal("1000.00")
        );

        assertTrue(result);
    }

    @Test
    void testProcessTransfer_InvalidAccountNumber() {

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> bankTransferService.processTransfer(
                        "123",
                        "SBIN0001234",
                        new BigDecimal("1000.00")
                )
        );

        assertEquals(
                "Invalid recipient account number",
                exception.getMessage()
        );
    }

    @Test
    void testProcessTransfer_InvalidIfsc() {

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> bankTransferService.processTransfer(
                        "123456789012",
                        "INVALID123",
                        new BigDecimal("1000.00")
                )
        );

        assertEquals(
                "Invalid IFSC code",
                exception.getMessage()
        );
    }
}