package com.PaymentService.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.PaymentService.Event.PaymentCompletedEvent;
import com.PaymentService.Exception.RefundFailedException;
import com.PaymentService.Exception.TransactionNotFoundException;
import com.PaymentService.External.services.BookingServiceClient;
import com.PaymentService.External.services.UserServiceClient;
import com.PaymentService.Repository.PaymentTransactionRepository;
import com.PaymentService.dto.BookingDTO;
import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.dto.PaymentTransactionDTO;
import com.PaymentService.dto.RefundRequestDTO;
import com.PaymentService.dto.RefundResponseDTO;
import com.PaymentService.dto.UserDTO;
import com.PaymentService.entity.PaymentTransaction;
import com.netflix.discovery.converters.Auto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
public class PaymentServiceImpl implements PaymentService 
{
	@Autowired
    private  UserServiceClient userClient;
	@Autowired
    private  BookingServiceClient bookingClient;

    @Autowired
    private PaymentTransactionRepository repository;

    @Autowired
    private PaymentGatewayClient gatewayClient;

 
    @Autowired
    private ApplicationEventPublisher publisher; // ✅ Event publisher

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

        String gatewayTxnId = "TXN-" + UUID.randomUUID(); // ✅ Unique ID
        txn.setTransactionId(gatewayTxnId);
        txn.setPaymentStatus("SUCCESS");
        txn.setCompletedAt(LocalDateTime.now());

        repository.save(txn);

        // ✅ Publish custom event
        publisher.publishEvent(new PaymentCompletedEvent(this, gatewayTxnId));

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
    @Override
    public Page<PaymentTransaction> getTransactionsByUser(String userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }
    
    @Override
    public List<PaymentTransaction> getAllTransactions() {
        return repository.findAll();
    }


    @Override
    public List<PaymentTransactionDTO> getPaymentsByUserId(String userId) {
        return repository.findAllByUserId(userId).stream()
                   .map(this::toDto)
                   .peek(this::enrichWithBooking)
                   .collect(Collectors.toList());
    }

    @Override
    public PaymentTransactionDTO getPaymentByBookingId(String bookingId) {
        PaymentTransaction txn = repository.findByBookingId(bookingId)
                                    .orElseThrow(() -> new RuntimeException("No payment"));
        PaymentTransactionDTO dto = toDto(txn);
        enrichWithBooking(dto);
        enrichWithUser(dto);
        return dto;
    }

    private PaymentTransactionDTO toDto(PaymentTransaction txn) {
        PaymentTransactionDTO dto = new PaymentTransactionDTO();
        // copy properties...
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
        BookingDTO bk = bookingClient.getBookingById(dto.getBookingId());
        // optionally attach booking fields to DTO if extended
    }

    private void enrichWithUser(PaymentTransactionDTO dto) {
        UserDTO user = userClient.getUserById(java.util.UUID.fromString(dto.getUserId()));
        // optionally attach user fields to DTO
    }
}

////PaymentService.java
//@Service
//public class PaymentService {
//
//    private final Config.WebClientConfig webClientConfig;
//
//
//    PaymentService(Config.WebClientConfig webClientConfig) {
//        this.webClientConfig = webClientConfig;
//    }
//
//
// public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
//     // Call Booking Service to validate booking
//     String bookingUrl = "http://BOOKING-SERVICE/api/bookings/" + request.getBookingId();
//     BookingDTO booking = restTemplate.getForObject(bookingUrl, BookingDTO.class);
//
//     // Logic to initiate payment...
//     PaymentTransaction transaction = new PaymentTransaction();
//     transaction.setUserId(request.getUserId());
//     transaction.setBookingId(request.getBookingId());
//     transaction.setAmount(request.getAmount());
//     transaction.setPaymentStatus("SUCCESS");
//     transaction.setInitiatedAt(LocalDateTime.now());
//
//     // Save and then notify user
//     sendPaymentSuccessNotification(request.getUserId());
//
//     return new PaymentResponseDTO("txn123", "SUCCESS", "Payment processed.");
// }
//
// private void sendPaymentSuccessNotification(String userId) {
//     String userUrl = "http://USER-SERVICE/api/users/" + userId + "/contact";
//     UserContactDTO contact = restTemplate.getForObject(userUrl, UserContactDTO.class);
//
//     NotificationRequestDTO notification = new NotificationRequestDTO();
//     notification.setUserId(Long.parseLong(userId));
//     notification.setRecipient(contact.getEmail());
//     notification.setSubject("Payment Successful");
//     notification.setContent("Your payment has been processed.");
//     notification.setType(NotificationType.EMAIL);
//
//     // POST to Notification Service
//     restTemplate.postForEntity("http://NOTIFICATION-SERVICE/api/notifications", notification, Void.class);
// }
