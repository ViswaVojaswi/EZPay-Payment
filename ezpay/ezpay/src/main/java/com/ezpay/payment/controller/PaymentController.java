package com.ezpay.payment.controller;

import com.ezpay.payment.dto.BankTransferRequest;
import com.ezpay.payment.dto.PaymentResponse;
import com.ezpay.payment.dto.UpiPaymentRequest;
import com.ezpay.payment.entity.PaymentTransaction;
import com.ezpay.payment.repository.PaymentTransactionRepository;
import com.ezpay.payment.service.PaymentService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PaymentTransactionRepository transactionRepository;

    public PaymentController(
            PaymentService paymentService,
            PaymentTransactionRepository transactionRepository) {

        this.paymentService = paymentService;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/upi")
    public ResponseEntity<PaymentResponse> makeUpiPayment(
            @Valid @RequestBody UpiPaymentRequest request) {

        log.info(
                "Received UPI payment API request for User ID: {}",
                request.getUserId()
        );

        PaymentResponse response =
                paymentService.processUpiPayment(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/bank-transfer")
    public ResponseEntity<PaymentResponse> makeBankTransfer(
            @Valid @RequestBody BankTransferRequest request) {

        log.info(
                "Received bank transfer API request for User ID: {}",
                request.getUserId()
        );

        PaymentResponse response =
                paymentService.processBankTransfer(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<PaymentTransaction> getTransaction(
            @PathVariable Long transactionId) {

        log.info(
                "Transaction details requested. Transaction ID: {}",
                transactionId
        );

        return transactionRepository
                .findById(transactionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {

                    log.warn(
                            "Transaction not found. Transaction ID: {}",
                            transactionId
                    );

                    return ResponseEntity.notFound().build();
                });
    }
}