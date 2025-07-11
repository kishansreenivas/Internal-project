package com.PaymentService.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import com.PaymentService.Service.PaymentService;
import com.PaymentService.dto.*;
import com.PaymentService.entity.PaymentTransaction;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> initiatePayment(@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<RefundResponseDTO>> processRefund(@Valid @RequestBody RefundRequestDTO request) {
        RefundResponseDTO response = paymentService.processRefund(request);
        return ResponseEntity.ok(new ApiResponse<>(true, response));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PaymentTransaction>>> getAllTransactions() {
        List<PaymentTransaction> list = paymentService.getAllTransactions();
        return ResponseEntity.ok(new ApiResponse<>(true, list));
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<ApiResponse<?>> getStatus(@PathVariable String transactionId) {
        PaymentTransaction transaction = paymentService.getPaymentStatus(transactionId);
        if (transaction == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Transaction not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, transaction));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentTransaction>>> getUserTransactions(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<PaymentTransaction> transactions = paymentService.getTransactionsByUser(userId, pageable).getContent();
        return ResponseEntity.ok(new ApiResponse<>(true, transactions));
    }

    @GetMapping("/bookings/{bookingId}")
    @CircuitBreaker(name = "bookingService", fallbackMethod = "getPaymentByBookingFallback")
    public ResponseEntity<ApiResponse<?>> getPaymentByBooking(@PathVariable String bookingId) {
        PaymentTransactionDTO dto = paymentService.getPaymentByBookingId(bookingId);
        if (dto == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, Map.of("error", "Payment not found for booking")));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, dto));
    }
    
    @GetMapping("/user/{userId}")
    @CircuitBreaker(name = "userPaymentsService", fallbackMethod = "getPaymentsByUserFallback")
    public ResponseEntity<ApiResponse<List<PaymentTransactionDTO>>> getPaymentsByUser(@PathVariable String userId) {
        List<PaymentTransactionDTO> transactions = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, transactions));
    }

    //  Fallback method for user payment fetch
    public ResponseEntity<ApiResponse<List<PaymentTransactionDTO>>> getPaymentsByUserFallback(String userId, Throwable t) {
        PaymentTransactionDTO fallbackDTO = new PaymentTransactionDTO();
        fallbackDTO.setUserId(userId);
        fallbackDTO.setPaymentStatus("UNAVAILABLE And Fallback response: User payment data is temporarily unavailable");
        // fallbackDTO.setMessage("Fallback response: User payment data is temporarily unavailable.");
        return ResponseEntity.ok(
            new ApiResponse<>(false, List.of(fallbackDTO))
        );
    }
    // Fallback method for  get PaymentByBooking
    public ResponseEntity<ApiResponse<?>> getPaymentByBookingFallback(String bookingId, Throwable t) {
        Map<String, String> fallbackResponse = Map.of(
            "bookingId", bookingId,
            "error", "Fallback: Booking service is currently unavailable"
        );
        return ResponseEntity.ok(new ApiResponse<>(false, fallbackResponse));
    }

}
