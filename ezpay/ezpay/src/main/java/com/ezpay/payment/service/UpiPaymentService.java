package com.ezpay.payment.service;

import com.ezpay.payment.exception.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UpiPaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(UpiPaymentService.class);

    public boolean processPayment(
            String recipientUpiId,
            java.math.BigDecimal amount) {

        log.info(
                "Initiating UPI payment. Recipient UPI: {}, Amount: {}",
                recipientUpiId,
                amount
        );

        if (!isValidUpiId(recipientUpiId)) {
            log.warn(
                    "UPI payment rejected because recipient UPI ID is invalid: {}",
                    recipientUpiId
            );

            throw new PaymentException("Invalid recipient UPI ID");
        }

        /*
         * This is a simulated UPI service.
         * In the real application this method will call
         * an external UPI provider/API.
         */

        log.info(
                "UPI service processed payment successfully for recipient: {}",
                recipientUpiId
        );

        return true;
    }

    private boolean isValidUpiId(String upiId) {

        return upiId != null
                && upiId.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+$");
    }
}