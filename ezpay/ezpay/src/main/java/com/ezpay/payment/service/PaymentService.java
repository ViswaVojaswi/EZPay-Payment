package com.ezpay.payment.service;

import com.ezpay.payment.dto.BankTransferRequest;
import com.ezpay.payment.dto.PaymentResponse;
import com.ezpay.payment.dto.UpiPaymentRequest;
import com.ezpay.payment.entity.PaymentAccount;
import com.ezpay.payment.entity.PaymentTransaction;
import com.ezpay.payment.enums.TransactionStatus;
import com.ezpay.payment.exception.PaymentException;
import com.ezpay.payment.repository.PaymentAccountRepository;
import com.ezpay.payment.repository.PaymentTransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentService.class);

    private final PaymentAccountRepository accountRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final UpiPaymentService upiPaymentService;
    private final BankTransferService bankTransferService;

    public PaymentService(
            PaymentAccountRepository accountRepository,
            PaymentTransactionRepository transactionRepository,
            UpiPaymentService upiPaymentService,
            BankTransferService bankTransferService) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.upiPaymentService = upiPaymentService;
        this.bankTransferService = bankTransferService;
    }

    @Transactional
    public PaymentResponse processUpiPayment(
            UpiPaymentRequest request) {

        log.info(
                "UPI payment request received. User ID: {}, Amount: {}",
                request.getUserId(),
                request.getAmount()
        );

        PaymentAccount account =
                getPaymentAccount(request.getUserId());

        validateBalance(
                account,
                request.getAmount()
        );

        PaymentTransaction transaction =
                createUpiTransaction(request);

        transaction.setStatus(TransactionStatus.PROCESSING);

        transaction =
                transactionRepository.save(transaction);

        log.info(
                "UPI transaction created. Transaction ID: {}",
                transaction.getId()
        );

        boolean processed =
                upiPaymentService.processPayment(
                        request.getRecipientUpiId(),
                        request.getAmount()
                );

        if (!processed) {

            transaction.setStatus(
                    TransactionStatus.FAILED
            );

            transactionRepository.save(transaction);

            log.error(
                    "UPI payment failed. Transaction ID: {}",
                    transaction.getId()
            );

            throw new PaymentException(
                    "UPI payment failed"
            );
        }

        deductBalance(
                account,
                request.getAmount()
        );

        accountRepository.save(account);

        transaction.setStatus(
                TransactionStatus.COMPLETED
        );

        transactionRepository.save(transaction);

        log.info(
                "UPI payment completed successfully. Transaction ID: {}",
                transaction.getId()
        );

        return new PaymentResponse(
                transaction.getId(),
                "UPI payment successful",
                TransactionStatus.COMPLETED,
                request.getAmount()
        );
    }

    @Transactional
    public PaymentResponse processBankTransfer(
            BankTransferRequest request) {

        log.info(
                "Bank transfer request received. User ID: {}, Amount: {}",
                request.getUserId(),
                request.getAmount()
        );

        PaymentAccount account =
                getPaymentAccount(request.getUserId());

        validateBalance(
                account,
                request.getAmount()
        );

        PaymentTransaction transaction =
                createBankTransaction(request);

        transaction.setStatus(
                TransactionStatus.PROCESSING
        );

        transaction =
                transactionRepository.save(transaction);

        log.info(
                "Bank transfer transaction created. Transaction ID: {}",
                transaction.getId()
        );

        boolean processed =
                bankTransferService.processTransfer(
                        request.getRecipientAccountNumber(),
                        request.getIfscCode(),
                        request.getAmount()
                );

        if (!processed) {

            transaction.setStatus(
                    TransactionStatus.FAILED
            );

            transactionRepository.save(transaction);

            log.error(
                    "Bank transfer failed. Transaction ID: {}",
                    transaction.getId()
            );

            throw new PaymentException(
                    "Bank transfer failed"
            );
        }

        deductBalance(
                account,
                request.getAmount()
        );

        accountRepository.save(account);

        transaction.setStatus(
                TransactionStatus.COMPLETED
        );

        transactionRepository.save(transaction);

        log.info(
                "Bank transfer completed successfully. Transaction ID: {}",
                transaction.getId()
        );

        return new PaymentResponse(
                transaction.getId(),
                "Bank transfer successful",
                TransactionStatus.COMPLETED,
                request.getAmount()
        );
    }

    private PaymentAccount getPaymentAccount(Long userId) {

        return accountRepository
                .findByUserId(userId)
                .orElseThrow(() -> {

                    log.warn(
                            "Payment account not found. User ID: {}",
                            userId
                    );

                    return new PaymentException(
                            "Payment account not found"
                    );
                });
    }

    private void validateBalance(
            PaymentAccount account,
            BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {

            log.warn(
                    "Insufficient balance. User ID: {}, Requested Amount: {}, Available Balance: {}",
                    account.getUserId(),
                    amount,
                    account.getBalance()
            );

            throw new PaymentException(
                    "Insufficient funds"
            );
        }
    }

    private void deductBalance(
            PaymentAccount account,
            BigDecimal amount) {

        BigDecimal newBalance =
                account.getBalance().subtract(amount);

        account.setBalance(newBalance);

        log.info(
                "Account balance updated. User ID: {}, New Balance: {}",
                account.getUserId(),
                newBalance
        );
    }

    private PaymentTransaction createUpiTransaction(
            UpiPaymentRequest request) {

        PaymentTransaction transaction =
                new PaymentTransaction();

        transaction.setUserId(request.getUserId());
        transaction.setTransactionType("UPI");
        transaction.setRecipientUpiId(
                request.getRecipientUpiId()
        );
        transaction.setAmount(request.getAmount());
        transaction.setNote(request.getNote());
        transaction.setStatus(
                TransactionStatus.INITIATED
        );
        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        return transaction;
    }

    private PaymentTransaction createBankTransaction(
            BankTransferRequest request) {

        PaymentTransaction transaction =
                new PaymentTransaction();

        transaction.setUserId(request.getUserId());
        transaction.setTransactionType("BANK_TRANSFER");
        transaction.setRecipientAccountNumber(
                request.getRecipientAccountNumber()
        );
        transaction.setIfscCode(
                request.getIfscCode()
        );
        transaction.setAmount(request.getAmount());
        transaction.setPurpose(request.getPurpose());
        transaction.setStatus(
                TransactionStatus.INITIATED
        );
        transaction.setCreatedAt(
                LocalDateTime.now()
        );

        return transaction;
    }
}