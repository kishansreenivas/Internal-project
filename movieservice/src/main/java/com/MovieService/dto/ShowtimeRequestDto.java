package com.MovieService.dto;


import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class ShowtimeRequestDto {
    private String movieId;
    private String screenId;
    private String theatreId;
    private LocalDateTime showStart;
    private LocalDateTime showEnd;
    private String language;

    // Getters and Setters
}
