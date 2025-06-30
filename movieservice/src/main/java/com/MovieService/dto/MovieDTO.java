package com.MovieService.dto;


import com.MovieService.EnumType.MovieStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private String id;
    private String title;
    private String language;
    private LocalDate releaseDate;
    private Integer durationMinutes;
    private MovieStatus status;
}
