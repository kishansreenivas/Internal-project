package com.UserService.Entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
	 @Id
	    @GeneratedValue
	    private UUID id;
    private String cardNumber;
    private String cardHolder;
    private String expiry;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // ✅ ManyToOne - PaymentMethod belongs to one user

}