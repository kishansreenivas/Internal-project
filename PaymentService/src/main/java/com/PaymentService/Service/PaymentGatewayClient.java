package com.PaymentService.Service;

import org.springframework.stereotype.Component;

import com.PaymentService.dto.PaymentRequestDTO;

@Component
public class PaymentGatewayClient {

    public String initiateExternalPayment(PaymentRequestDTO request) {
        return "TXN-" + System.currentTimeMillis();
    }

    public boolean refundTransaction(String transactionId) {
        return true; // Simulate success
    }
}