package com.PaymentService;

import com.PaymentService.Event.PaymentCompletedEvent;
import com.PaymentService.Exception.RefundFailedException;
import com.PaymentService.Exception.TransactionNotFoundException;
import com.PaymentService.External.services.BookingServiceClient;
import com.PaymentService.External.services.UserServiceClient;
import com.PaymentService.Repository.PaymentTransactionRepository;
import com.PaymentService.Service.PaymentGatewayClient;
import com.PaymentService.Service.impl.PaymentServiceImpl;
import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.dto.RefundRequestDTO;
import com.PaymentService.dto.RefundResponseDTO;
import com.PaymentService.entity.PaymentTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentTransactionRepository repository;

    @Mock
    private PaymentGatewayClient gatewayClient;

    @Mock
    private ApplicationEventPublisher publisher;

    @Mock
    private UserServiceClient userClient;

    @Mock
    private BookingServiceClient bookingClient;

    @Mock
    private ModelMapper modelMapper;

    private PaymentRequestDTO paymentRequest;
    private RefundRequestDTO refundRequest;
    private PaymentTransaction paymentTxn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentRequest = new PaymentRequestDTO();
        paymentRequest.setBookingId("booking-123");
        paymentRequest.setUserId("user-123");
        paymentRequest.setAmount(BigDecimal.valueOf(100.0));
        paymentRequest.setCurrency("INR");
        paymentRequest.setPaymentMethod("UPI");

        refundRequest = new RefundRequestDTO();
        refundRequest.setTransactionId("TXN-123");

        paymentTxn = new PaymentTransaction();
        paymentTxn.setTransactionId("TXN-123");
        paymentTxn.setAmount(BigDecimal.valueOf(100));
        paymentTxn.setPaymentStatus("SUCCESS");
    }

    @Test
    void testInitiatePayment_Success() {
        when(repository.save(any(PaymentTransaction.class))).thenAnswer(i -> i.getArguments()[0]);

        PaymentResponseDTO response = paymentService.initiatePayment(paymentRequest);

        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertNotNull(response.getTransactionId());
        assertEquals("Payment processed successfully.", response.getMessage());

        verify(repository, times(1)).save(any(PaymentTransaction.class));
        verify(publisher, times(1)).publishEvent(any(PaymentCompletedEvent.class));
    }

    @Test
    void testInitiatePayment_InvalidAmount_ShouldThrow() {
        paymentRequest.setAmount(BigDecimal.ZERO);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                paymentService.initiatePayment(paymentRequest)
        );

        assertEquals("Amount must be greater than 0", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void testProcessRefund_Success() {
        when(repository.findByTransactionId("TXN-123")).thenReturn(Optional.of(paymentTxn));
        when(gatewayClient.refundTransaction("TXN-123")).thenReturn(true);
        when(repository.save(any(PaymentTransaction.class))).thenReturn(paymentTxn);

        RefundResponseDTO response = paymentService.processRefund(refundRequest);

        assertNotNull(response);
        assertEquals("REFUNDED", response.getStatus());
        assertEquals("TXN-123", response.getRefundTransactionId());

        verify(gatewayClient).refundTransaction("TXN-123");
        verify(repository).save(paymentTxn);
    }

    @Test
    void testProcessRefund_TransactionNotFound_ShouldThrow() {
        when(repository.findByTransactionId("TXN-123")).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
                paymentService.processRefund(refundRequest)
        );

        verify(gatewayClient, never()).refundTransaction(any());
    }

    @Test
    void testProcessRefund_RefundFails_ShouldThrow() {
        when(repository.findByTransactionId("TXN-123")).thenReturn(Optional.of(paymentTxn));
        when(gatewayClient.refundTransaction("TXN-123")).thenReturn(false);

        assertThrows(RefundFailedException.class, () ->
                paymentService.processRefund(refundRequest)
        );

        verify(gatewayClient).refundTransaction("TXN-123");
    }
}
