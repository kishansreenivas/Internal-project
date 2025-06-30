package com.BookingService.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentTransaction {

    private String bookingId;
    private String userId;

    private BigDecimal amount;
    private String currency;
    private String paymentStatus; 
    private String paymentMethod;

    private String transactionId;
    private String gatewayResponse;

    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
}
