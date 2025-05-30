package com.BookingService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.BookingService.Dto.BookingRequest;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;
import com.BookingService.Service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired private BookingService bookingService;

    @PostMapping("/initiate")
    public Booking initiateBooking(@RequestBody BookingRequest request) {
        return bookingService.initiateBooking(request.getUserId(), request.getShowId(), request.getScreenId(), request.getSeatIds(), request.getTotalAmount());
    }

    @PostMapping("/confirm/{bookingId}")
    public Booking confirm(@PathVariable String bookingId, @RequestParam String paymentId) {
        return bookingService.confirmBooking(bookingId, paymentId);
    }

    @PostMapping("/cancel/{bookingId}")
    public Booking cancel(@PathVariable String bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/seats/{bookingId}")
    public List<BookedSeat> getSeatsForBooking(@PathVariable String bookingId) {
        return bookingService.getSeatsByBookingId(bookingId);
    }
}
