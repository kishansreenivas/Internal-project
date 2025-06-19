package com.MovieService.Services;


import com.MovieService.Entity.Showtime;
import com.MovieService.dto.ShowtimeRequestDto;

import java.util.List;

public interface ShowtimeService {
    Showtime createShowtime(ShowtimeRequestDto dto);
    List<Showtime> getAllShowtimes();
    Showtime getShowtimeById(String id);
    Showtime updateShowtime(String id, Showtime updated);
    void deleteShowtime(String id);
}
