package com.BookingService.Dto;

import java.time.LocalDateTime;
import java.util.List;

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
}
