package com.NotificationService.External.Service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.NotificationService.dto.UserContactDTO;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/v1/users/contact/{userId}")
    UserContactDTO getUserContact(@PathVariable("userId") String userId);
}
