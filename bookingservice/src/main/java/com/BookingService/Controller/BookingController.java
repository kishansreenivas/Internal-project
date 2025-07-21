package com.BookingService.Controller;

import com.BookingService.Dto.BookingDTO;
import com.BookingService.Dto.BookingRequest;
import com.BookingService.Dto.BookingResponse;
import com.BookingService.Dto.BookingWithSeatsDTO;
import com.BookingService.Dto.ScreenDto;
import com.BookingService.Dto.ShowtimeDto;
import com.BookingService.Dto.UserDTO;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;

import com.BookingService.Service.Impl.BookingServiceImpl;
import com.BookingService.constants.BookingMessages;
import com.BookingService.mapper.BookingMapper;
import com.BookingService.payload.ApiResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@Slf4j
@RestController
@RequestMapping("/v1/bookings")
public class BookingController {

   
    @Autowired
    private BookingServiceImpl bookingService;
    
    @Autowired
    private BookingMapper bookingMapper;

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
            log.info("Booking successfully initiated: bookingId={}, userId={}, showId={}, screenId={}, seatIds={}, totalAmount={}",
                    booking.getBookingId(),
                    request.getUserId(),
                    request.getShowId(),
                    request.getScreenId(),
                    request.getSeatIds(),
                    request.getTotalAmount());
            return ResponseEntity.ok(ApiResponse.success(BookingMessages.BOOKING_INITIATED_SUCCESS, booking));
        } catch (Exception e) {
            log.error("Failed to initiate booking", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure(BookingMessages.BOOKING_CANCELLATION_FAILED + e.getMessage()));
        }
    }

    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<ApiResponse<Booking>> confirmBooking(@Valid @PathVariable String bookingId, @RequestParam String paymentId) {
        try {
            log.info("Confirming bookingId={} with paymentId={}", bookingId, paymentId);
            Booking confirmed = bookingService.confirmBooking(bookingId, paymentId);
            return ResponseEntity.ok(ApiResponse.success(BookingMessages.BOOKING_CONFIRMED_SUCCESS, confirmed));
        } catch (Exception e) {
            log.error("Booking confirmation failed", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure(BookingMessages.BOOKING_CONFIRMATION_FAILED + e.getMessage()));
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
    public ResponseEntity<ApiResponse<UserDTO>> fetchUserProfile(@PathVariable UUID userId) {
        try {
            UserDTO user = bookingService.getUserDetails(userId);
            return ResponseEntity.ok(ApiResponse.success("User profile fetched", user));
        } catch (Exception e) {
            log.error("Failed to fetch user profile", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch user: " + e.getMessage()));
        }
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<ApiResponse<List<BookingDTO>>> getBookingsByUser(@PathVariable String userId) {
//        try {
//            List<Booking> bookings = bookingService.getBookingsByUserId(userId);
//
//            List<BookingDTO> bookingDtos = bookings.stream()
//                .map(bookingMapper::toDto) // uses modelMapper internally
//                .collect(Collectors.toList());
//
//            return ResponseEntity.ok(ApiResponse.success("User bookings fetched", bookingDtos));
//        } catch (Exception e) {
//            log.error("Failed to get bookings by user", e);
//            return ResponseEntity
//                .badRequest()
//                .body(ApiResponse.failure("Failed to get bookings: " + e.getMessage()));
//        }
//    }

//    @GetMapping("/users/{userId}")
//    public ResponseEntity<ApiResponse<UserDto>> getBookingsByUserWithId(@PathVariable UUID userId) {
//        try {
//            UserDto user = bookingService.getUserDetails(userId);
//            List<Booking> bookings = bookingService.getBookingsByUserId(userId.toString());
//
//            List<Long> bookingIds = bookings.stream()
//                    .map(b -> Long.valueOf(b.getBookingId().hashCode())) 
//                    .collect(Collectors.toList());
//
//            user.setBookingIds(bookingIds);
//            return ResponseEntity.ok(ApiResponse.success("User and bookings fetched", user));
//        } catch (Exception e) {
//            log.error("Failed to get user with bookings", e);
//            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch user/bookings: " + e.getMessage()));
//        }
//    }

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

    @GetMapping("/Allwithseats")
    public ResponseEntity<ApiResponse<List<BookingWithSeatsDTO>>> getAllBookingsWithSeats() {
        try {
            List<BookingWithSeatsDTO> bookings = bookingService.getAllBookingsWithSeats();
            return ResponseEntity.ok(ApiResponse.success("Bookings with seats fetched", bookings));
        } catch (Exception e) {
            log.error("Failed to get all bookings with seats", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch bookings: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/release-locks")
    public ResponseEntity<ApiResponse<String>> releaseExpiredLocks() {
        try {
            log.info("Releasing expired seat locks");
            bookingService.releaseExpiredLocks();
            return ResponseEntity.ok(ApiResponse.success("Expired seat locks released", "Success"));
        } catch (Exception e) {
            log.error("Failed to release expired locks", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to release locks: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Page<Booking>>> listBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Booking> bookings = bookingService.listBookings(PageRequest.of(page, size));
            return ResponseEntity.ok(ApiResponse.success("Paginated bookings fetched", bookings));
        } catch (Exception e) {
            log.error("Failed to fetch paginated bookings", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch bookings: " + e.getMessage()));
        }
    }
    
    @GetMapping("/screen/{screenId}")
    public ResponseEntity<ApiResponse<ScreenDto>> getScreenDetails(@PathVariable String screenId) {
        try {
            ScreenDto screen = bookingService.fetchScreenDetails(screenId);
            return ResponseEntity.ok(ApiResponse.success("Screen details fetched", screen));
        } catch (Exception e) {
            log.error("Failed to fetch screen", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch screen: " + e.getMessage()));
        }
    }

    @GetMapping("/showtime/{showId}")
    public ResponseEntity<ApiResponse<ShowtimeDto>> getShowtimeDetails(@PathVariable String showId) {
        try {
            ShowtimeDto dto = bookingService.fetchShowtimeDetails(showId);
            return ResponseEntity.ok(ApiResponse.success("Showtime details fetched", dto));
        } catch (Exception e) {
            log.error("Failed to fetch showtime", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Failed to fetch showtime: " + e.getMessage()));
        }
    }

    
    @GetMapping("/response/{bookingId}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingResponse(@PathVariable String bookingId) {
        try {
            Booking booking = bookingService.confirmBooking(bookingId, null); // if null paymentId is safe
            BookingResponse response = bookingService.mapToResponse(booking);
            return ResponseEntity.ok(ApiResponse.success("Mapped booking response", response));
        } catch (Exception e) {
            log.error("Failed to map booking response", e);
            return ResponseEntity.badRequest().body(ApiResponse.failure("Mapping failed: " + e.getMessage()));
        }
    }
  
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getBookingsByUser(@PathVariable String userId) {
        try {
            ApiResponse<List<BookingDTO>> response = bookingService.getBookingsByUserId(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get bookings by user", e);
            return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure("Failed to get bookings: " + e.getMessage()));
        }
    }

}
