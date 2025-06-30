package com.UserService.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//com.UserService.Dto.MovieDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
 private String id;
 private String title;
 private String language;
 private LocalDate releaseDate;
 private Integer durationMinutes;
 private String status;
}
