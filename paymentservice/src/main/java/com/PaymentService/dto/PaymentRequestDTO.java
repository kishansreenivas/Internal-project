package com.PaymentService.dto;

import java.math.BigDecimal;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PaymentRequestDTO {
    private String bookingId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
}
