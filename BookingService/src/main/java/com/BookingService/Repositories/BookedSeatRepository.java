package com.BookingService.Repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BookingService.Entities.BookedSeat;
import com.BookingService.Enum.BookingStatus;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Long> {
    boolean existsBySeatIdAndStatusIn(String seatId, List<BookingStatus> statuses);
    List<BookedSeat> findAllByStatusAndLockedAtBefore(BookingStatus status, LocalDateTime time);
    List<BookedSeat> findAllByBookingBookingId(String bookingId);
}
