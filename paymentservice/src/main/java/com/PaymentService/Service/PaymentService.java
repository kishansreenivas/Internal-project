package com.PaymentService.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.dto.PaymentTransactionDTO;
import com.PaymentService.dto.RefundRequestDTO;
import com.PaymentService.dto.RefundResponseDTO;
import com.PaymentService.entity.PaymentTransaction;

public interface PaymentService {
    PaymentResponseDTO initiatePayment(PaymentRequestDTO request);
    RefundResponseDTO processRefund(RefundRequestDTO request);
    PaymentTransaction getPaymentStatus(String transactionId);
	Page<PaymentTransaction> getTransactionsByUser(String userId, Pageable pageable);
	List<PaymentTransaction> getAllTransactions();
	List<PaymentTransactionDTO> getPaymentsByUserId(String userId);
    PaymentTransactionDTO getPaymentByBookingId(String bookingId);
}
