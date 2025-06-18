package com.PaymentService.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;
    private String userId;

    private BigDecimal amount;
    private String currency;
    private String paymentStatus; // INITIATED, SUCCESS, FAILED, REFUNDED
    private String paymentMethod;

    private String transactionId;
    private String gatewayResponse;

    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
}