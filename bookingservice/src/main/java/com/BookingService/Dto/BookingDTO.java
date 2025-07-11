package com.BookingService.Dto;

import java.time.LocalDateTime;

import com.BookingService.Enum.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingDTO {

	    private String bookingId;
	    private String showId;
	    private String screenId;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
	    private LocalDateTime bookingTime;
	    private String paymentId;
	    private double totalAmount;
	    private BookingStatus status;
	   
	}


