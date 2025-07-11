package com.BookingService.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


 @Getter
 @Setter
 @NoArgsConstructor
 @AllArgsConstructor
@Data
public class BookingRequest {
	
    private List<String> seatIds;
   
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Show ID is required")
    private String showId;

    @NotBlank(message = "Screen ID is required")
    private String screenId;

    @NotNull(message = "Booking time is required")
    
    
    private LocalDateTime bookingTime;

    private String paymentId; 

    @Positive(message = "Total amount must be greater than zero")
    private double totalAmount;

}

