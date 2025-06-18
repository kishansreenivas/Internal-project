package com.UserService.Services;

import java.util.List;
import java.util.UUID;

import com.UserService.Dto.PaymentMethodDTO;

public interface PaymentMethodService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto);
    PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodDTO dto);
    PaymentMethodDTO getPaymentMethodById(UUID id);
    List<PaymentMethodDTO> getAllPaymentMethods();
    void deletePaymentMethod(UUID id);
}
