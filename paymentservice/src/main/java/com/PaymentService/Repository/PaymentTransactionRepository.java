package com.PaymentService.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.PaymentService.entity.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByTransactionId(String transactionId);
    Page<PaymentTransaction> findAllByUserId(String userId, Pageable pageable);
    List<PaymentTransaction> findAllByUserId(String userId);
    Optional<PaymentTransaction> findByBookingId(String bookingId);

}