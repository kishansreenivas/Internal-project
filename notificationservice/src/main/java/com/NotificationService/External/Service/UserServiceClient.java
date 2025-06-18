package com.NotificationService.External.Service;


import com.NotificationService.dto.UserContactDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/users")
    UserContactDTO getUserContact(@PathVariable("userId") Long userId);
}
