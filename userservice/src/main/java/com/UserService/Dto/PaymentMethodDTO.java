package com.UserService.Dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	  @NotBlank(message = "Card number is required")
	    @Pattern(regexp = "^\\d{13,19}$", message = "Card number must be 13 to 19 digits")
	    private String cardNumber;

	    @NotBlank(message = "Card holder name is required")
	    private String cardHolder;

	    @NotBlank(message = "Expiry date is required")
	    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Expiry must be in MM/YY format")
	    private String expiry;

	    @NotBlank(message = "Payment type is required")
	    private String type;

	    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
       private UUID userId;

}