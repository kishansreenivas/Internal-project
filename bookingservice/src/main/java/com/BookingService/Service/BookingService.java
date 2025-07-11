package com.BookingService.Service;

import com.BookingService.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;
import com.BookingService.payload.ApiResponse;
import com.BookingService.Dto.ScreenDto;
import com.BookingService.Dto.ShowtimeDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    Booking initiateBooking(String userId, String showId, String screenId, List<String> seatIds, double totalAmount);
    Booking confirmBooking(String bookingId, String paymentId);
    Booking cancelBooking(String bookingId);
    void releaseExpiredLocks();
    List<BookedSeat> getSeatsByBookingId(String bookingId);
    List<BookedSeat> getAllBookedSeats();
    UserDto getUserDetails(UUID userId);
    ApiResponse<List<BookingDTO>> getBookingsByUserId(String userId);
    BookingResponse mapToResponse(Booking booking);
    List<BookingWithSeatsDTO> getAllBookingsWithSeats();
    Page<Booking> listBookings(Pageable pageable);
    ScreenDto fetchScreenDetails(String screenId);
    ShowtimeDto fetchShowtimeDetails(String showId);
   
}
