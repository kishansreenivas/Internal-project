package com.UserService.Dto;

import java.time.LocalDateTime;

import com.UserService.constants.MovieStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//com.UserService.Dto.MovieDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
	  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String title;
    private String language;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime releaseDate;
    private Integer durationMinutes;
    private MovieStatus status;
}
