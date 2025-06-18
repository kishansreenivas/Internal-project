package com.PaymentService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentTransactionDTO {
    private Long id;
    private String bookingId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
}
