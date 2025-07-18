package com.MovieService.dto;


import com.MovieService.EnumType.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private String id;
    private String title;
    private String language;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private Integer durationMinutes;
    private MovieStatus status;
}
