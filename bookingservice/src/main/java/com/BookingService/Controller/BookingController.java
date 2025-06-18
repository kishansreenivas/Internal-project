package com.BookingService.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.BookingService.Dto.BookingRequest;
import com.BookingService.Dto.BookingWithSeatsDTO;
import com.BookingService.Dto.UserDto;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;
import com.BookingService.Service.BookingService;

@RestController
@RequestMapping("/v1/bookings")
public class BookingController {
    @Autowired 
    private BookingService bookingService;
 
    @PostMapping("/initiate")
    public ResponseEntity<Booking> initiateBooking(@RequestBody BookingRequest request) {
        Booking booking = bookingService.initiateBooking(
            request.getUserId(),
            request.getShowId(),
            request.getScreenId(),
            request.getSeatIds(),
            request.getTotalAmount()
        );
        return ResponseEntity.ok(booking);
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
    

    // Return user details only
    @GetMapping("/user/profile/{userId}")
    public UserDto fetchUserProfile(@PathVariable UUID userId) {
        return bookingService.getUserDetails(userId);
    }


    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(@PathVariable String userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping("/users/{userId}")
    public UserDto getUserWithBookings(@PathVariable UUID userId) {
        UserDto user = bookingService.getUserDetails(userId);
        List<Booking> bookings = bookingService.getBookingsByUserId(userId.toString());

        // Check if bookingService.getBookingsByUserId is working
        List<Long> bookingIds = bookings.stream()
                .map(b -> Long.valueOf(b.getBookingId().hashCode())) // ⚠️ This is suspicious!
                .collect(Collectors.toList());

        user.setBookingIds(bookingIds);
        return user;
    }

    @GetMapping("/seats")
    public List<BookedSeat> getAllBookedSeats() {
        return bookingService.getAllBookedSeats();
    }
    
    @GetMapping("/All-with-seats")
    public List<BookingWithSeatsDTO> getAllBookingsWithSeats() {
        return bookingService.getAllBookingsWithSeats();
    }

}
