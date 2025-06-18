package com.PaymentService.External.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.PaymentService.dto.UserDTO;

import java.util.UUID;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

 @GetMapping("/{id}")
 UserDTO getUserById(@PathVariable("id") UUID id);
}
