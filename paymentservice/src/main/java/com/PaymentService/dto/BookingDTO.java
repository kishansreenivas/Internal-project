package com.PaymentService.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private String bookingId;
    private String userId;
    private String showId;
    private String screenId;
    private LocalDateTime bookingTime;
    private String paymentId;
    private double totalAmount;
    private String status;
    private List<Long> seatIds;
}
