package com.UserService.Dto;

import java.util.UUID;

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
	private UUID id;
    private String cardNumber;
    private String cardHolder;
    private String expiry;
    private String type;
    private UUID userId;
}