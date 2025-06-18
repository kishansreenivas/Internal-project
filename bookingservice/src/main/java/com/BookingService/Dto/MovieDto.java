package com.BookingService.Dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private String id;
    private String title;
    private String language;
    private LocalDate releaseDate;
    private Integer durationMinutes;
}
