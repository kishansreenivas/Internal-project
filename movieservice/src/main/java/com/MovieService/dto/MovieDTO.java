package com.MovieService.dto;


import com.MovieService.EnumType.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss.SSS")
    private LocalDate releaseDate;
    private Integer durationMinutes;
    private MovieStatus status;
}
