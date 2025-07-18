package com.BookingService.External.Service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.BookingService.Dto.MovieDto;
import com.BookingService.Dto.ScreenDto;
import com.BookingService.Dto.ShowtimeDto;
import com.BookingService.Interceptor.MovieClientConfig;
import com.BookingService.payload.ApiResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "MOVIE-SERVICE",configuration = MovieClientConfig.class)
public interface MovieClient {
	
	
	@PostMapping("/v1/movies")
    ApiResponse<MovieDto> createMovie(@RequestBody MovieDto movie);

    @GetMapping("/v1/movies")
    ApiResponse<List<MovieDto>> getAllMovies();

    @GetMapping("/v1/movies/{id}")
    ApiResponse<MovieDto> getMovieById(@PathVariable("id") String id);

    @PutMapping("/v1/movies/{id}")
    ApiResponse<MovieDto> updateMovie(@PathVariable("id") String id, @RequestBody MovieDto movie);

    @DeleteMapping("/v1/movies/{id}")
    ApiResponse<Map<String, String>> deleteMovie(@PathVariable("id") String id);
	    
    @GetMapping("/screens/{screenId}")
    ScreenDto getScreenById(@PathVariable("screenId") String screenId);

    @GetMapping("/showtimes/{showId}")
    ShowtimeDto getShowtimeById(@PathVariable("showId") String showId);
}
