package com.BookingService.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;
import com.BookingService.Enum.BookingStatus;
import com.BookingService.Repositories.BookedSeatRepository;
import com.BookingService.Repositories.BookingRepository;

import Exception.ResourceNotFoundException;
import Exception.SeatAlreadyBookedException;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepo;
    @Autowired private BookedSeatRepository seatRepo;

    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    public Booking initiateBooking(String userId, String showId, String screenId, List<String> seatIds, double totalAmount) {
        List<BookedSeat> lockedSeats = new ArrayList<>();

        for (String seatId : seatIds) {
            if (seatRepo.existsBySeatIdAndStatusIn(seatId, List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED))) {
                throw new SeatAlreadyBookedException("Seat already locked/booked: " + seatId);
            }
            BookedSeat seat = new BookedSeat();
            seat.setSeatId(seatId);
            seat.setScreenId(screenId);
            seat.setStatus(BookingStatus.PENDING);
            seat.setLockedAt(LocalDateTime.now());
            lockedSeats.add(seat);
        }

        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setUserId(userId);
        booking.setShowId(showId);
        booking.setScreenId(screenId);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(totalAmount);

        for (BookedSeat seat : lockedSeats) {
            seat.setBooking(booking);
        }

        booking.setSeats(lockedSeats);
        return bookingRepo.save(booking);
    }

    public Booking confirmBooking(String bookingId, String paymentId) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        booking.getSeats().forEach(seat -> seat.setStatus(BookingStatus.CONFIRMED));
        return bookingRepo.save(booking);
    }

    public Booking cancelBooking(String bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSeats().forEach(seat -> seat.setStatus(BookingStatus.CANCELLED));
        return bookingRepo.save(booking);
    }

    public void releaseExpiredLocks() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minus(LOCK_DURATION);
        List<BookedSeat> expiredSeats = seatRepo.findAllByStatusAndLockedAtBefore(BookingStatus.PENDING, expiryThreshold);
        expiredSeats.forEach(seat -> seat.setStatus(BookingStatus.FAILED));
        seatRepo.saveAll(expiredSeats);
    }

    public List<BookedSeat> getSeatsByBookingId(String bookingId) {
        return seatRepo.findAllByBookingBookingId(bookingId);
    }
}
