package com.ezpay.payment.service;

import com.ezpay.payment.exception.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankTransferService {

    private static final Logger log =
            LoggerFactory.getLogger(BankTransferService.class);

    public boolean processTransfer(
            String accountNumber,
            String ifscCode,
            BigDecimal amount) {

        log.info(
                "Initiating bank transfer. Account: {}, IFSC: {}, Amount: {}",
                maskAccountNumber(accountNumber),
                ifscCode,
                amount
        );

        if (!isValidAccountNumber(accountNumber)) {

            log.warn(
                    "Bank transfer rejected because account number is invalid"
            );

            throw new PaymentException(
                    "Invalid recipient account number"
            );
        }

        if (!isValidIfsc(ifscCode)) {

            log.warn(
                    "Bank transfer rejected because IFSC code is invalid: {}",
                    ifscCode
            );

            throw new PaymentException(
                    "Invalid IFSC code"
            );
        }

        /*
         * This is a simulated banking service.
         * In the real application this method will call
         * an external banking API.
         */

        log.info(
                "Banking service processed transfer successfully. Account: {}",
                maskAccountNumber(accountNumber)
        );

        return true;
    }

    private boolean isValidAccountNumber(String accountNumber) {

        return accountNumber != null
                && accountNumber.matches("^[0-9]{9,18}$");
    }

    private boolean isValidIfsc(String ifscCode) {

        return ifscCode != null
                && ifscCode.matches("^[A-Z]{4}0[A-Z0-9]{6}$");
    }

    private String maskAccountNumber(String accountNumber) {

        if (accountNumber == null || accountNumber.length() < 4) {
            return "****";
        }

        return "********" +
                accountNumber.substring(accountNumber.length() - 4);
    }
}