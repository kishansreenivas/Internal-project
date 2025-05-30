package com.PaymentService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PaymentService.entity.PaymentTransaction;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
}