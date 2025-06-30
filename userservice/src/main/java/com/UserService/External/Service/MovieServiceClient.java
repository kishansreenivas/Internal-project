package com.UserService.External.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.MovieDTO;

//com.UserService.External.Service.MovieServiceClient.java
@FeignClient(name = "MovieService")
public interface MovieServiceClient {
	
 @GetMapping("/v1/movies/{id}")
 ApiResponse<MovieDTO> getMovieById(@PathVariable("id") String id);
}
