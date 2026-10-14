package com.ezpay.payment.repository;

import com.ezpay.payment.entity.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentAccountRepository
        extends JpaRepository<PaymentAccount, Long> {

    Optional<PaymentAccount> findByUserId(Long userId);
}