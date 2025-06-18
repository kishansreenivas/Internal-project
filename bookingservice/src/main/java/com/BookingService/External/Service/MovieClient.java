package com.BookingService.External.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.BookingService.Dto.ScreenDto;
import com.BookingService.Dto.ShowtimeDto;

@FeignClient(name = "MOVIE-SERVICE")
public interface MovieClient {
    @GetMapping("/screens/{screenId}")
    ScreenDto getScreenById(@PathVariable("screenId") String screenId);

    @GetMapping("/showtimes/{showId}")
    ShowtimeDto getShowtimeById(@PathVariable("showId") String showId);
}
