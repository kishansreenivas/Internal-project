package com.UserService.Dto;


import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BookingDto {
 private String bookingId;
 private UUID userId;
 private Long showId;
 private Long screenId;
 private List<Long> seatIds;
 private Double totalAmount;
 private String status;
}
