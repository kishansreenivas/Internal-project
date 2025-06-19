package com.PaymentService.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PaymentService.Event.PaymentCompletedEvent;
import com.PaymentService.Exception.RefundFailedException;
import com.PaymentService.Exception.TransactionNotFoundException;
import com.PaymentService.External.services.BookingServiceClient;
import com.PaymentService.External.services.UserServiceClient;
import com.PaymentService.Repository.PaymentTransactionRepository;
import com.PaymentService.dto.*;
import com.PaymentService.entity.PaymentTransaction;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private UserServiceClient userClient;

    @Autowired
    private BookingServiceClient bookingClient;

    @Autowired
    private PaymentTransactionRepository repository;

    @Autowired
    private PaymentGatewayClient gatewayClient;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request) {
        log.info("ENTRY: initiatePayment() - request = {}", request);

        // VALIDATION
        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            log.warn("VALIDATION FAILED: Invalid payment amount: {}", request.getAmount());
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }

        PaymentTransaction txn = new PaymentTransaction();
        txn.setBookingId(request.getBookingId());
        txn.setUserId(request.getUserId());
        txn.setAmount(request.getAmount());
        txn.setCurrency(request.getCurrency());
        txn.setPaymentMethod(request.getPaymentMethod());
        txn.setInitiatedAt(LocalDateTime.now());
        txn.setPaymentStatus("INITIATED");

        String gatewayTxnId = "TXN-" + UUID.randomUUID();
        txn.setTransactionId(gatewayTxnId);
        txn.setPaymentStatus("SUCCESS");
        txn.setCompletedAt(LocalDateTime.now());

        repository.save(txn);

        publisher.publishEvent(new PaymentCompletedEvent(this, gatewayTxnId));

        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setTransactionId(gatewayTxnId);
        response.setStatus("SUCCESS");
        response.setMessage("Payment processed successfully.");

        log.info("EXIT: initiatePayment() - response = {}", response);
        return response;
    }

    @Override
    public RefundResponseDTO processRefund(RefundRequestDTO request) {
        log.info("ENTRY: processRefund() - request = {}", request);

        PaymentTransaction txn = repository.findByTransactionId(request.getTransactionId())
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Transaction not found: {}", request.getTransactionId());
                    return new TransactionNotFoundException("Transaction not found");
                });

        boolean refunded = gatewayClient.refundTransaction(request.getTransactionId());
        if (!refunded) {
            log.error("Refund failed at gateway for transaction: {}", request.getTransactionId());
            throw new RefundFailedException("Refund failed at payment gateway");
        }

        txn.setPaymentStatus("REFUNDED");
        repository.save(txn);

        RefundResponseDTO response = new RefundResponseDTO();
        response.setRefundTransactionId(txn.getTransactionId());
        response.setStatus("REFUNDED");

        log.info("EXIT: processRefund() - response = {}", response);
        return response;
    }

    @Override
    public PaymentTransaction getPaymentStatus(String transactionId) {
        log.info("ENTRY: getPaymentStatus() - transactionId = {}", transactionId);

        PaymentTransaction txn = repository.findByTransactionId(transactionId)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Transaction not found: {}", transactionId);
                    return new TransactionNotFoundException("Transaction not found");
                });

        log.info("EXIT: getPaymentStatus() - transaction = {}", txn);
        return txn;
    }

    @Override
    public Page<PaymentTransaction> getTransactionsByUser(String userId, Pageable pageable) {
        log.info("ENTRY: getTransactionsByUser() - userId = {}", userId);
        Page<PaymentTransaction> transactions = repository.findAllByUserId(userId, pageable);
        log.info("EXIT: getTransactionsByUser() - count = {}", transactions.getTotalElements());
        return transactions;
    }

    @Override
    public List<PaymentTransaction> getAllTransactions() {
        log.info("ENTRY: getAllTransactions()");
        List<PaymentTransaction> list = repository.findAll();
        log.info("EXIT: getAllTransactions() - count = {}", list.size());
        return list;
    }

    @Override
    public List<PaymentTransactionDTO> getPaymentsByUserId(String userId) {
        log.info("ENTRY: getPaymentsByUserId() - userId = {}", userId);
        List<PaymentTransactionDTO> list = repository.findAllByUserId(userId).stream()
                .map(this::toDto)
                .peek(this::enrichWithBooking)
                .collect(Collectors.toList());
        log.info("EXIT: getPaymentsByUserId() - count = {}", list.size());
        return list;
    }

    @Override
    public PaymentTransactionDTO getPaymentByBookingId(String bookingId) {
        log.info("ENTRY: getPaymentByBookingId() - bookingId = {}", bookingId);

        PaymentTransaction txn = repository.findByBookingId(bookingId)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: No payment found for bookingId = {}", bookingId);
                    return new TransactionNotFoundException("No payment found for this booking ID");
                });

        PaymentTransactionDTO dto = toDto(txn);
        enrichWithBooking(dto);
        enrichWithUser(dto);

        log.info("EXIT: getPaymentByBookingId() - dto = {}", dto);
        return dto;
    }

    private PaymentTransactionDTO toDto(PaymentTransaction txn) {
        PaymentTransactionDTO dto = new PaymentTransactionDTO();
        dto.setId(txn.getId());
        dto.setBookingId(txn.getBookingId());
        dto.setUserId(txn.getUserId());
        dto.setAmount(txn.getAmount());
        dto.setCurrency(txn.getCurrency());
        dto.setPaymentStatus(txn.getPaymentStatus());
        dto.setPaymentMethod(txn.getPaymentMethod());
        dto.setTransactionId(txn.getTransactionId());
        dto.setInitiatedAt(txn.getInitiatedAt());
        dto.setCompletedAt(txn.getCompletedAt());
        return dto;
    }

    private void enrichWithBooking(PaymentTransactionDTO dto) {
        log.debug("Enriching with booking info for bookingId = {}", dto.getBookingId());
        BookingDTO bk = bookingClient.getBookingById(dto.getBookingId());
        // optionally attach booking fields
    }

    private void enrichWithUser(PaymentTransactionDTO dto) {
        log.debug("Enriching with user info for userId = {}", dto.getUserId());
        UserDTO user = userClient.getUserById(UUID.fromString(dto.getUserId()));
        // optionally attach user fields
    }
}
