package com.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PaymentMethodDTO {
    private Long id;
    private String cardNumber;
    private String cardHolder;
    private String expiry;
    private String type;
}