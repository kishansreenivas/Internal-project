package com.BookingService.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDto {
    private String id;
    private MovieDto movie;
    private TheatreDto theatre;
    private ScreenDto screen;
    private LocalDateTime showStart;
    private LocalDateTime showEnd;
    private String language;
}
