package com.BookingService.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BookingService.Entities.BookedSeat;
import com.BookingService.constants.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface BookedSeatRepository extends JpaRepository<BookedSeat, String> {
 boolean existsBySeatIdAndStatusIn(String seatId, List<BookingStatus> statuses);
 List<BookedSeat> findAllByStatusAndLockedAtBefore(BookingStatus status, LocalDateTime time);
 List<BookedSeat> findAllByBookingBookingId(String bookingId);
}
