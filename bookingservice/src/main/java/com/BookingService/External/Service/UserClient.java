package com.BookingService.External.Service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.BookingService.Dto.UserDTO;


import java.util.UUID;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
//    @GetMapping("/api/users/{id}")
//    UserDto getUserById(@PathVariable("id") UUID id);
//    

    @GetMapping("/v1/users/{id}")
    UserDTO getUserById(@PathVariable("id") UUID userId);
    
    @GetMapping("/v1/users/{userId}")
    UserDTO getUserWithBookings(@PathVariable("userId") UUID userId);  // ✅ Correct UUID and path
    
}
