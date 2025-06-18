package com.PaymentService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.given;

import com.PaymentService.Repository.PaymentTransactionRepository;
import com.PaymentService.Service.PaymentGatewayClient;
import com.PaymentService.Service.PaymentServiceImpl;
import com.PaymentService.dto.PaymentRequestDTO;
import com.PaymentService.dto.PaymentResponseDTO;
import com.PaymentService.entity.PaymentTransaction;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentTransactionRepository repository;

    @Mock
    private PaymentGatewayClient gatewayClient;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    public PaymentServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitiatePayment() {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setBookingId("BKG1");
        request.setUserId("USER1");
        request.setAmount(new BigDecimal("150.00"));
        request.setCurrency("INR");
        request.setPaymentMethod("CARD");

        when(gatewayClient.initiateExternalPayment(any())).thenReturn("TXN-TEST-123");

        PaymentResponseDTO response = paymentService.initiatePayment(request);

        assertEquals("SUCCESS", response.getStatus());
        assertEquals("TXN-TEST-123", response.getTransactionId());
        verify(repository, times(1)).save(any(PaymentTransaction.class));
    }
    @Test
    void testInitiatePayment_shouldReturnSuccess_givenValidRequest() {
        // Given
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setBookingId("BKG-1");
        request.setUserId("USR-1");
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("INR");
        request.setPaymentMethod("CARD");

        given(gatewayClient.initiateExternalPayment(any())).willReturn("TXN-" + UUID.randomUUID());

        // When
        PaymentResponseDTO response = paymentService.initiatePayment(request);

        // Then
        assertThat(response.getStatus()).isEqualTo("SUCCESS");
        assertThat(response.getTransactionId()).startsWith("TXN-");
    }

}
