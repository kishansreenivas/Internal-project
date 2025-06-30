package com.UserService.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Services.PaymentMethodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentMethodDTO>> create(@RequestBody PaymentMethodDTO dto) {
        PaymentMethodDTO saved = paymentMethodService.createPaymentMethod(dto);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodDTO>> update(@PathVariable UUID id, @RequestBody PaymentMethodDTO dto) {
        PaymentMethodDTO updated = paymentMethodService.updatePaymentMethod(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentMethodDTO>> getById(@PathVariable UUID id) {
        PaymentMethodDTO result = paymentMethodService.getPaymentMethodById(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentMethodDTO>>> getAll() {
        List<PaymentMethodDTO> list = paymentMethodService.getAllPaymentMethods();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok(ApiResponse.success("Payment method deleted successfully."));
    }
}
