package com.ezpay.payment.repository;

import com.ezpay.payment.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {
}