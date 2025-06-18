package com.UserService.External.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.UserService.Dto.BookingDto;
import com.UserService.Dto.UserDTO;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "Booking-Service") // Must match spring.application.name in BookingService
public interface BookingServiceClient {

    @PostMapping("/api/bookings/initiate")
    BookingDto initiateBooking(@RequestBody BookingDto request);

    @PostMapping("/api/bookings/confirm/{bookingId}")
    BookingDto confirmBooking(@PathVariable String bookingId, @RequestParam String paymentId);

    @PostMapping("/api/bookings/cancel/{bookingId}")
    BookingDto cancelBooking(@PathVariable String bookingId);

    @GetMapping("/api/bookings/seats/{bookingId}")
    List<String> getSeatsForBooking(@PathVariable String bookingId);

    @GetMapping("/with-bookings/{userId}")
    UserDTO getUserWithBookings(@PathVariable("userId") UUID userId);  // ✅ Correct UUID and path
     
}
