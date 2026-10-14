package com.ezpay.payment.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ezpay.payment.exception.PaymentException;

class UpiPaymentServiceTest {

    private UpiPaymentService upiPaymentService;

    @BeforeEach
    void setUp() {
        upiPaymentService = new UpiPaymentService();
    }

    @Test
    void testProcessPayment_ValidUpi() {

        boolean result = upiPaymentService.processPayment(
                "rahul@upi",
                new BigDecimal("500.00")
        );

        assertTrue(result);
    }

    @Test
    void testProcessPayment_InvalidUpi() {

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> upiPaymentService.processPayment(
                        "invalid-upi",
                        new BigDecimal("500.00")
                )
        );

        assertEquals("Invalid recipient UPI ID", exception.getMessage());
    }

    @Test
    void testProcessPayment_NullUpi() {

        PaymentException exception = assertThrows(
                PaymentException.class,
                () -> upiPaymentService.processPayment(
                        null,
                        new BigDecimal("500.00")
                )
        );

        assertEquals("Invalid recipient UPI ID", exception.getMessage());
    }
}