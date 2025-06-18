package com.PaymentService.dto;

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

public class PaymentResponseDTO {
    private String transactionId;
    private String status;
    private String message;
}
