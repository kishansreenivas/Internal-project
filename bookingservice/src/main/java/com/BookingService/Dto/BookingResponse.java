package com.BookingService.Dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.BookingService.Entities.BookedSeat;
import com.BookingService.Entities.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private String bookingId;
    private String userId;
    private String showId;
    private String screenId;
    private LocalDateTime bookingTime;
    private String paymentId;
    private double totalAmount;
    private String status;
    private List<BookedSeatResponse> seats;
    
    public BookingResponse(Booking booking, List<BookedSeat> seats2) {
        this.bookingId = booking.getBookingId();
        this.userId = booking.getUserId();
        this.showId = booking.getShowId();
        this.screenId = booking.getScreenId();
        this.bookingTime = booking.getBookingTime();
        this.paymentId = booking.getPaymentId();
        this.totalAmount = booking.getTotalAmount();
        this.status = booking.getStatus().name();

        this.seats = seats2.stream()
            .map(seat -> new BookedSeatResponse(seat.getSeatId()))
            .collect(Collectors.toList());
    }
 }
