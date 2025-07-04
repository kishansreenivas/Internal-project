package com.UserService.External.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDto;
import com.UserService.Dto.UserDTO;

@FeignClient(name = "BOOKING-SERVICE") // Must match spring.application.name in BookingService
public interface BookingServiceClient {

	@GetMapping("/v1/bookings/user/{userId}")
    ApiResponse<List<BookingDto>> getBookingsByUser(@PathVariable("userId") String userId);
	
//	@GetMapping("/v1/bookings/seats")
//    ApiResponse<List<BookingDto>> getAllBookedSeats();
//	
}
