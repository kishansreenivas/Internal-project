package com.BookingService.External.Service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.BookingService.Dto.PaymentTransaction;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentServiceClient {

 @GetMapping("/status/{transactionId}")
 PaymentTransaction getPaymentStatus(@PathVariable("transactionId") String transactionId);

 @GetMapping("/bookings/{bookingId}")
 PaymentTransaction getPaymentByBookingId(@PathVariable("bookingId") String bookingId);
}
