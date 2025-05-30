package com.PaymentService.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.PaymentService.Exception.RefundFailedException;
import com.PaymentService.Exception.TransactionNotFoundException;
import com.PaymentService.Repository.PaymentTransactionRepository;
import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.dto.RefundRequestDTO;
import com.PaymentService.dto.RefundResponseDTO;
import com.PaymentService.entity.PaymentTransaction;

@Service
public class PaymentServiceImpl implements PaymentService 
{

    @Autowired
    private PaymentTransactionRepository repository;

    @Autowired
    private PaymentGatewayClient gatewayClient;

    @Override
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request) {
        PaymentTransaction txn = new PaymentTransaction();
        txn.setBookingId(request.getBookingId());
        txn.setUserId(request.getUserId());
        txn.setAmount(request.getAmount());
        txn.setCurrency(request.getCurrency());
        txn.setPaymentMethod(request.getPaymentMethod());
        txn.setInitiatedAt(LocalDateTime.now());
        txn.setPaymentStatus("INITIATED");

        String gatewayTxnId = gatewayClient.initiateExternalPayment(request);
        txn.setTransactionId(gatewayTxnId);
        txn.setPaymentStatus("SUCCESS");
        txn.setCompletedAt(LocalDateTime.now());

        repository.save(txn);

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setTransactionId(gatewayTxnId);
        response.setStatus("SUCCESS");
        response.setMessage("Payment processed successfully.");
        return response;
    }

    @Override
    public RefundResponseDTO processRefund(RefundRequestDTO request) {
        PaymentTransaction txn = repository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));

        boolean refunded = gatewayClient.refundTransaction(request.getTransactionId());
        if (!refunded) {
            throw new RefundFailedException("Refund failed at payment gateway");
        }

        txn.setPaymentStatus("REFUNDED");
        repository.save(txn);

        RefundResponseDTO response = new RefundResponseDTO();
        response.setRefundTransactionId(txn.getTransactionId());
        response.setStatus("REFUNDED");
        return response;
    }

    @Override
    public PaymentTransaction getPaymentStatus(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }
}