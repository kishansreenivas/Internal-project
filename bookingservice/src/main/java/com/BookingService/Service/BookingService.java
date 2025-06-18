package com.BookingService.Service;

import com.BookingService.Dto.*;
import com.BookingService.Entities.*;
import com.BookingService.Enum.BookingStatus;
import com.BookingService.Event.BookingConfirmedEvent;
import com.BookingService.Exception.ResourceNotFoundException;
import com.BookingService.Exception.SeatAlreadyBookedException;
import com.BookingService.External.Service.MovieClient;
import com.BookingService.External.Service.UserClient;
import com.BookingService.Repositories.BookedSeatRepository;
import com.BookingService.Repositories.BookingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private BookedSeatRepository seatRepo;
    @Autowired private UserClient userClient;
    @Autowired private ApplicationEventPublisher pub;
    
    @Autowired
    private MovieClient movieClient;

    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    public Booking initiateBooking(String userId, String showId, String screenId,
                                   List<String> seatIds, double totalAmount) {

        seatIds.forEach(seatId -> {
            if (seatRepo.existsBySeatIdAndStatusIn(seatId, List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED))) {
                throw new SeatAlreadyBookedException("Seat already locked/booked: " + seatId);
            }
        });

        Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setUserId(userId);
        booking.setShowId(showId);
        booking.setScreenId(screenId);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalAmount(totalAmount);

        List<BookedSeat> seats = seatIds.stream().map(seatId -> {
            BookedSeat seat = new BookedSeat();
            seat.setSeatId(seatId);
            seat.setScreenId(screenId);
            seat.setStatus(BookingStatus.PENDING);
            seat.setLockedAt(LocalDateTime.now());
            seat.setBooking(booking);
            return seat;
        }).collect(Collectors.toList());

        booking.setSeats(seats);
        return bookingRepo.save(booking);
    }

    public Booking confirmBooking(String bookingId, String paymentId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        booking.getSeats().forEach(seat -> seat.setStatus(BookingStatus.CONFIRMED));
        pub.publishEvent(new BookingConfirmedEvent(this, bookingId));
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

    public List<BookedSeat> getAllBookedSeats() {
        return seatRepo.findAll();
    }

    public UserDto getUserDetails(UUID userId) {
        return userClient.getUserById(userId);
    }

    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepo.findByUserId(userId);
    }

    public BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setUserId(booking.getUserId());
        response.setShowId(booking.getShowId());
        response.setScreenId(booking.getScreenId());
        response.setBookingTime(booking.getBookingTime());
        response.setPaymentId(booking.getPaymentId());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus().name());

        List<BookedSeatResponse> seatResponses = booking.getSeats().stream()
                .map(seat -> new BookedSeatResponse(seat.getSeatId()))
                .collect(Collectors.toList());

        response.setSeats(seatResponses);
        return response;
    }

    public List<BookingWithSeatsDTO> getAllBookingsWithSeats() {
        return bookingRepo.findAll().stream()
                .map(b -> new BookingWithSeatsDTO(b.getBookingId(), b.getSeats()))
                .collect(Collectors.toList());
    }

    public Page<Booking> listBookings(Pageable pageable) {
        return bookingRepo.findAll(pageable);
    }
 

    public ScreenDto fetchScreenDetails(String screenId) {
        return movieClient.getScreenById(screenId);
    }

    public ShowtimeDto fetchShowtimeDetails(String showId) {
        return movieClient.getShowtimeById(showId);
    }

}
