package com.UserService.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.User;

@Component
public class PaymentMethodMapper {
	

    private final ModelMapper modelMapper;

    @Autowired
    public PaymentMethodMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PaymentMethodDTO toDto(PaymentMethod paymentMethod) {
        PaymentMethodDTO dto = modelMapper.map(paymentMethod, PaymentMethodDTO.class);
        if (paymentMethod.getUser() != null) {
            dto.setUserId(paymentMethod.getUser().getId()); // manually set userId
        }
        return dto;
    }

    public PaymentMethod toEntity(PaymentMethodDTO dto, User user) {
        PaymentMethod entity = modelMapper.map(dto, PaymentMethod.class);
        entity.setUser(user); // manually set user reference
        return entity;
    }

}
