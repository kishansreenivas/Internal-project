package com.UserService.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Services.PaymentMethodService;

import java.util.List;
import java.util.UUID;

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
