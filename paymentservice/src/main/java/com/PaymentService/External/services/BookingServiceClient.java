package com.PaymentService.External.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.PaymentService.dto.BookingDTO;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingServiceClient {

 @GetMapping("/v1/bookings/seats/{bookingId}")
 BookingDTO getSeatsForBooking(@PathVariable("bookingId") String bookingId);
}
