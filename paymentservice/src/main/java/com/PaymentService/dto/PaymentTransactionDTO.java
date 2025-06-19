package com.PaymentService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransactionDTO {
    private Long id;
    private String bookingId;
    private String userId;
    
    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency must not be blank")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    private String currency;

    @NotBlank(message = "Payment status must not be blank")
    private String paymentStatus;

    @NotBlank(message = "Payment method must not be blank")
    private String paymentMethod;

    @NotBlank(message = "Transaction ID must not be blank")
    private String transactionId;
    
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

}
