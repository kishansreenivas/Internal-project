package com.BookingService.Service.Impl;

import com.BookingService.Service.BookingService;
import com.BookingService.payload.ApiResponse;
import com.BookingService.Dto.*;
import com.BookingService.Entities.*;
import com.BookingService.constants.BookingMessages;
import com.BookingService.constants.BookingStatus;
import com.BookingService.Event.BookingConfirmedEvent;
import com.BookingService.Exception.*;
import com.BookingService.External.Service.MovieClient;
import com.BookingService.External.Service.UserClient;
import com.BookingService.Repositories.*;

import org.modelmapper.ModelMapper;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import jakarta.validation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
   
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    @Autowired private BookingRepository bookingRepo;
    @Autowired private BookedSeatRepository seatRepo;
    @Autowired private UserClient userClient;
    @Autowired private MovieClient movieClient;
    @Autowired private ApplicationEventPublisher pub;
    @Autowired private ModelMapper modelMapper;

    public BookingServiceImpl() {
        log.info(" BookingServiceImpl instantiated");
    }

    /**
     * Initiates a booking for the user with the specified details.
     * <p>
     * This method validates input parameters, checks for seat availability, 
     * creates a new booking, and saves it to the repository. It also generates 
     * a unique booking ID and associates the specified seats with the booking.
     * </p>
     *
     * @param userId      The ID of the user initiating the booking.
     * @param showId      The ID of the show for which the booking is made.
     * @param screenId    The ID of the screen where the show will be played.
     * @param seatIds     A list of seat IDs selected for booking.
     * @param totalAmount The total amount to be paid for the booking.
     * @return            The saved booking object containing the generated booking ID and associated seats.
     * @throws NullPointerException      If any of the userId, showId, or screenId are null.
     * @throws ConstraintViolationException If the seatIds list is empty or the totalAmount is non-positive.
     * @throws SeatAlreadyBookedException If any of the selected seats are already booked or pending.
     */
    @Override
    public Booking initiateBooking(String userId, String showId, String screenId,
                                   List<String> seatIds, double totalAmount) {
        log.info("ENTRY initiateBooking(userId={}, showId={}, screenId={}, seats={}, amount={})",
                userId, showId, screenId, seatIds, totalAmount);

        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(showId, "showId must not be null");
        Objects.requireNonNull(screenId, "screenId must not be null");
        if (seatIds.isEmpty() || totalAmount <= 0) {
            throw new ConstraintViolationException("Seats list or totalAmount invalid", Set.of());
        }

        for (String seatId : seatIds) {
            if (seatRepo.existsBySeatIdAndStatusIn(seatId, List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED))) {
                throw new SeatAlreadyBookedException("Seat already locked/booked: " + seatId);
            }
        }

        Booking booking = new Booking(UUID.randomUUID().toString(), userId, showId, screenId,
                LocalDateTime.now(), BookingStatus.PENDING, null, totalAmount, null);

        List<BookedSeat> seats = seatIds.stream().map(seatId -> {
            BookedSeat seat = new BookedSeat(seatId, screenId, BookingStatus.PENDING, LocalDateTime.now(), booking);
            return seat;
        }).collect(Collectors.toList());

        booking.setSeats(seats);
        Booking saved = bookingRepo.save(booking);
        log.info("EXIT initiateBooking => saved bookingId={}", saved.getBookingId());
        return saved;
    }
    
    /**
     * Confirms a booking by updating its status and associating it with a payment ID.
     * <p>
     * This method retrieves the booking by its ID, changes its status to confirmed, 
     * associates the payment ID with the booking, and updates the status of all 
     * associated seats to confirmed. A {@link BookingConfirmedEvent} is published 
     * to notify that the booking has been confirmed. Finally, the updated booking 
     * is saved to the repository.
     * </p>
     *
     * @param bookingId The ID of the booking to be confirmed.
     * @param paymentId The payment ID associated with the confirmed booking.
     * @return          The updated booking with the confirmed status.
     * @throws ResourceNotFoundException If the booking with the specified bookingId does not exist.
     */
    @Override
    public Booking confirmBooking(String bookingId, String paymentId) {
        log.info("ENTRY confirmBooking(bookingId={}, paymentId={})", bookingId, paymentId);

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentId(paymentId);
        booking.getSeats().forEach(s -> s.setStatus(BookingStatus.CONFIRMED));
        pub.publishEvent(new BookingConfirmedEvent(this, bookingId));
        Booking updated = bookingRepo.save(booking);

        log.info("EXIT confirmBooking => status={}", updated.getStatus());
        return updated;
    }
    
    /**
     * Cancels an existing booking by updating its status and the status of all associated seats.
     * <p>
     * This method retrieves the booking by its ID, changes its status to cancelled, 
     * and updates the status of all associated seats to cancelled. The updated booking 
     * is then saved to the repository.
     * </p>
     *
     * @param bookingId The ID of the booking to be cancelled.
     * @return          The updated booking with the cancelled status.
     * @throws ResourceNotFoundException If the booking with the specified bookingId does not exist.
     */
    @Override
    public Booking cancelBooking(String bookingId) {
        log.warn("ENTRY cancelBooking(bookingId={})", bookingId);
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getSeats().forEach(s -> s.setStatus(BookingStatus.CANCELLED));
        Booking cancelled = bookingRepo.save(booking);

        log.warn("EXIT cancelBooking => status={}", cancelled.getStatus());
        return cancelled;
    }

    @Override
    public void releaseExpiredLocks() {
        log.info("ENTRY releaseExpiredLocks()");
        LocalDateTime expiry = LocalDateTime.now().minus(LOCK_DURATION);
        List<BookedSeat> expired = seatRepo.findAllByStatusAndLockedAtBefore(BookingStatus.PENDING, expiry);
        expired.forEach(s -> s.setStatus(BookingStatus.FAILED));
        seatRepo.saveAll(expired);
        log.info("EXIT releaseExpiredLocks => releasedCount={}", expired.size());
    }
    
    /**
     * Releases expired seat locks by updating their status to FAILED.
     * <p>
     * This method identifies seats that are currently locked with the status {@link BookingStatus#PENDING} 
     * and whose lock time has passed the defined expiration period. The status of these seats is updated to 
     * {@link BookingStatus#FAILED}, and the changes are saved to the repository.
     * </p>
     *
     * @throws None. This method does not throw any exceptions.
     */
    @Override
    public List<BookedSeat> getSeatsByBookingId(String bookingId) {
        log.info("ENTRY getSeatsByBookingId({})", bookingId);
        List<BookedSeat> list = seatRepo.findAllByBookingBookingId(bookingId);
        log.info("EXIT getSeatsByBookingId => foundCount={}", list.size());
        return list;
    }

    @Override
    public List<BookedSeat> getAllBookedSeats() {
        log.info("ENTRY getAllBookedSeats()");
        List<BookedSeat> list = seatRepo.findAll();
        log.info("EXIT getAllBookedSeats => totalCount={}", list.size());
        return list;
    }

    @Override
    public UserDTO  getUserDetails(UUID userId) {
        log.info("ENTRY getUserDetails({})", userId);
        UserDTO user = userClient.getUserById(userId);
        log.info("EXIT getUserDetails => username={}", user.getFirstName());
        return user;
    }


    @Override
    public ApiResponse<List<BookingDTO>> getBookingsByUserId(String userId) {
        log.info("ENTRY getBookingsByUserId({})", userId);

        try {
            List<Booking> bookings = bookingRepo.findByUserId(userId);

            List<BookingDTO> bookingDtos = bookings.stream()
                    .map(booking -> modelMapper.map(booking, BookingDTO.class))
                    .collect(Collectors.toList());

            log.info("EXIT getBookingsByUserId => count={}", bookingDtos.size());
            return ApiResponse.success(BookingMessages.USER_BOOKINGS_FETCHED, bookingDtos);

        } catch (Exception e) {
            log.error("Failed to fetch bookings for user {}", userId, e);
            return ApiResponse.failure(BookingMessages.USER_BOOKINGS_FETCH_FAILED + ": " + e.getMessage());
        }
    }


    @Override
    public BookingResponse mapToResponse(Booking booking) {
        log.info("ENTRY mapToResponse(bookingId={})", booking.getBookingId());
        BookingResponse response = new BookingResponse(booking, booking.getSeats());
        log.info("EXIT mapToResponse => status={}", response.getStatus());
        return response;
    }

    @Override
    public List<BookingWithSeatsDTO> getAllBookingsWithSeats() {
        log.info("ENTRY getAllBookingsWithSeats()");
        List<BookingWithSeatsDTO> list = bookingRepo.findAll().stream()
                .map(b -> new BookingWithSeatsDTO(b.getBookingId(), b.getSeats()))
                .collect(Collectors.toList());
        log.info("EXIT getAllBookingsWithSeats => count={}", list.size());
        return list;
    }

    @Override
    public Page<Booking> listBookings(Pageable pageable) {
        log.info("ENTRY listBookings(page={},size={})", pageable.getPageNumber(), pageable.getPageSize());
        Page<Booking> page = bookingRepo.findAll(pageable);
        log.info("EXIT listBookings => totalElements={}", page.getTotalElements());
        return page;
    }

    @Override
    public ScreenDto fetchScreenDetails(String screenId) {
        log.info("ENTRY fetchScreenDetails({})", screenId);
        ScreenDto dto = movieClient.getScreenById(screenId);
        log.info("EXIT fetchScreenDetails => name={}", dto.getName());
        return dto;
    }

    @Override
    public ShowtimeDto fetchShowtimeDetails(String showId) {
        log.info("ENTRY fetchShowtimeDetails({})", showId);
        ShowtimeDto dto = movieClient.getShowtimeById(showId);
        log.info("EXIT fetchShowtimeDetails => start={}", dto.getShowStart());
        return dto;
    }
    
} 
