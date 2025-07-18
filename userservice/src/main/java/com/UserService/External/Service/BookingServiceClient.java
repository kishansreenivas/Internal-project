package com.UserService.External.Service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDTO;
import com.UserService.Interceptor.BookingClientConfig;

@FeignClient(name = "BOOKING-SERVICE", configuration = BookingClientConfig.class , url = "http://localhost:9994/v1/bookings")
public interface BookingServiceClient {

	@GetMapping("/user/{userId}")
    ApiResponse<List<BookingDTO>> getBookingsByUser(@PathVariable("userId") String userId);

//	@GetMapping("/v1/bookings/seats")
//    ApiResponse<List<BookingDto>> getAllBookedSeats();
//
}
