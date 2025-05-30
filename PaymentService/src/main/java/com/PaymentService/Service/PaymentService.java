package com.PaymentService.Service;

import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.dto.RefundRequestDTO;
import com.PaymentService.dto.RefundResponseDTO;
import com.PaymentService.entity.PaymentTransaction;

public interface PaymentService {
    PaymentResponseDTO initiatePayment(PaymentRequestDTO request);
    RefundResponseDTO processRefund(RefundRequestDTO request);
    PaymentTransaction getPaymentStatus(String transactionId);
}
