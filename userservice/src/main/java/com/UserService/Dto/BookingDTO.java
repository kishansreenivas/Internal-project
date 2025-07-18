package com.UserService.Dto;

import java.time.LocalDateTime;

import com.UserService.Enum.BookingStatus;
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

	    private BookingDTO getDummyBooking() {
	        BookingDTO dummy = new BookingDTO();
	        dummy.setBookingId("N/A");
	        dummy.setShowId("DUMMY_SHOW");
	        dummy.setScreenId("DUMMY_SCREEN");
	        dummy.setBookingTime(LocalDateTime.now());
	        dummy.setPaymentId("DUMMY_PAYMENT");
	        dummy.setTotalAmount(0.0);
	        dummy.setStatus(BookingStatus.CANCELLED);
	        return dummy;
	    }

}
