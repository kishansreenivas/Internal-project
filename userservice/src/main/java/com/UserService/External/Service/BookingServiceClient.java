package com.UserService.External.Service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDto;


@FeignClient(name = "BOOKING-SERVICE") // Must match spring.application.name in BookingService
public interface BookingServiceClient {

	@GetMapping("/v1/bookings/user/{userId}")
 public  ApiResponse<List<BookingDto>> getBookingsByUser(@PathVariable("userId") String userId);
	
//	@GetMapping("/v1/bookings/seats")
//    ApiResponse<List<BookingDto>> getAllBookedSeats();
//	
}
