package com.PaymentService.External.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.PaymentService.dto.BookingDTO;

@FeignClient(name = "Booking-Service")
public interface BookingServiceClient {

 @GetMapping("/api/bookings/{bookingId}")
 BookingDTO getBookingById(@PathVariable("bookingId") String bookingId);
}
