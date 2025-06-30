package com.UserService.Dto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.UserService.Enum.BookingStatus;

import lombok.Data;

@Data
public class BookingDto {
	  private String bookingId;
	    private String showId;
	    private String screenId;
	    private LocalDateTime bookingTime;
	    private String paymentId;
	    private double totalAmount;
	    private BookingStatus status;
	    
	    private BookingDto getDummyBooking() {
	        BookingDto dummy = new BookingDto();
	        dummy.setBookingId("N/A");
	        dummy.setShowId("DUMMY_SHOW");
	        dummy.setScreenId("DUMMY_SCREEN");
	        dummy.setBookingTime(LocalDateTime.now());
	        dummy.setPaymentId("DUMMY_PAYMENT");
	        dummy.setTotalAmount(0.0);
	        dummy.setStatus(BookingStatus.CANCELLED); // Or any default enum value
	        return dummy;
	    }

}
