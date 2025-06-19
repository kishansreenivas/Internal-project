package com.BookingService.Controller;

import com.BookingService.Dto.BookingRequest;
import com.BookingService.Dto.BookingWithSeatsDTO;
import com.BookingService.Dto.UserDto;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;

import com.BookingService.Service.BookingService;
import com.BookingService.payload.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/v1/bookings")
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;

    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<Booking>> initiateBooking(@Valid @RequestBody BookingRequest request) {
        try {
            log.info("Initiating booking for userId={}, showId={}, screenId={}", request.getUserId(), request.getShowId(), request.getScreenId());
            Booking booking = bookingService.initiateBooking(
                    request.getUserId(),
                    request.getShowId(),
                    request.getScreenId(),
                    request.getSeatIds(),
                    request.getTotalAmount()
            );
            return ResponseEntity.ok(ApiResponse.success("Booking initiated successfully", booking));
        } catch (Exception e) {
            log.error("Failed to initiate booking", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to initiate booking: " + e.getMessage()));
        }
    }

    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> confirm(@Valid @PathVariable String bookingId, @RequestParam String paymentId) {
        try {
            log.info("Confirming bookingId={} with paymentId={}", bookingId, paymentId);
            Booking confirmed = bookingService.confirmBooking(bookingId, paymentId);
            return ResponseEntity.ok(ApiResponse.success("Booking confirmed", confirmed));
        } catch (Exception e) {
            log.error("Booking confirmation failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Booking confirmation failed: " + e.getMessage()));
        }
    }

    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> cancel(@Valid @PathVariable String bookingId) {
        try {
            log.info("Cancelling bookingId={}", bookingId);
            Booking cancelled = bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Booking cancelled", cancelled));
        } catch (Exception e) {
            log.error("Failed to cancel booking", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to cancel booking: " + e.getMessage()));
        }
    }

    @GetMapping("/seats/{bookingId}")
    public ResponseEntity<ApiResponse<List<BookedSeat>>> getSeatsForBooking(@PathVariable String bookingId) {
        try {
            List<BookedSeat> seats = bookingService.getSeatsByBookingId(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Fetched seats", seats));
        } catch (Exception e) {
            log.error("Failed to get seats", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to get seats: " + e.getMessage()));
        }
    }

    @GetMapping("/user/profile/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> fetchUserProfile(@PathVariable UUID userId) {
        try {
            UserDto user = bookingService.getUserDetails(userId);
            return ResponseEntity.ok(ApiResponse.success("User profile fetched", user));
        } catch (Exception e) {
            log.error("Failed to fetch user profile", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch user: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getBookingsByUser(@PathVariable String userId) {
        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success("User bookings fetched", bookings));
        } catch (Exception e) {
            log.error("Failed to get bookings by user", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to get bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserWithBookings(@PathVariable UUID userId) {
        try {
            UserDto user = bookingService.getUserDetails(userId);
            List<Booking> bookings = bookingService.getBookingsByUserId(userId.toString());

            List<Long> bookingIds = bookings.stream()
                    .map(b -> Long.valueOf(b.getBookingId().hashCode())) // ⚠️ Consider using UUIDs instead
                    .collect(Collectors.toList());

            user.setBookingIds(bookingIds);
            return ResponseEntity.ok(ApiResponse.success("User and bookings fetched", user));
        } catch (Exception e) {
            log.error("Failed to get user with bookings", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch user/bookings: " + e.getMessage()));
        }
    }

    @GetMapping("/seats")
    public ResponseEntity<ApiResponse<List<BookedSeat>>> getAllBookedSeats() {
        try {
            List<BookedSeat> seats = bookingService.getAllBookedSeats();
            return ResponseEntity.ok(ApiResponse.success("All booked seats fetched", seats));
        } catch (Exception e) {
            log.error("Failed to get all booked seats", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to get booked seats: " + e.getMessage()));
        }
    }

    @GetMapping("/All-with-seats")
    public ResponseEntity<ApiResponse<List<BookingWithSeatsDTO>>> getAllBookingsWithSeats() {
        try {
            List<BookingWithSeatsDTO> bookings = bookingService.getAllBookingsWithSeats();
            return ResponseEntity.ok(ApiResponse.success("Bookings with seats fetched", bookings));
        } catch (Exception e) {
            log.error("Failed to get all bookings with seats", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch bookings: " + e.getMessage()));
        }
    }
}
