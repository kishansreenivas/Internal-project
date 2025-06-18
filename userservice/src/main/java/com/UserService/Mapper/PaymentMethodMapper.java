package com.UserService.Mapper;

import org.springframework.stereotype.Component;

import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.User;

@Component
public class PaymentMethodMapper {
    public PaymentMethodDTO toDto(PaymentMethod paymentMethod) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setId(paymentMethod.getId());
        dto.setCardNumber(paymentMethod.getCardNumber());
        dto.setCardHolder(paymentMethod.getCardHolder());
        dto.setExpiry(paymentMethod.getExpiry());
        dto.setType(paymentMethod.getType());
        if (paymentMethod.getUser() != null) {
            dto.setUserId(paymentMethod.getUser().getId());
        }
        return dto;
    }

    public PaymentMethod toEntity(PaymentMethodDTO dto, User user) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(dto.getId());
        paymentMethod.setCardNumber(dto.getCardNumber());
        paymentMethod.setCardHolder(dto.getCardHolder());
        paymentMethod.setExpiry(dto.getExpiry());
        paymentMethod.setType(dto.getType());
        paymentMethod.setUser(user);
        return paymentMethod;
    }
}
