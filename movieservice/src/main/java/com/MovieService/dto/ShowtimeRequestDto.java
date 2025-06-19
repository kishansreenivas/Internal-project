package com.MovieService.dto;


import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Getter
@Setter
public class ShowtimeRequestDto {
	  @NotBlank(message = "Movie ID is required")
	    private String movieId;

	    @NotBlank(message = "Screen ID is required")
	    private String screenId;

	    @NotBlank(message = "Theatre ID is required")
	    private String theatreId;

	    @NotNull(message = "Show start time is required")
	    @Future(message = "Show start time must be in the future")
	    private LocalDateTime showStart;

	    @NotNull(message = "Show end time is required")
	    @Future(message = "Show end time must be in the future")
	    private LocalDateTime showEnd;

	    @NotBlank(message = "Language is required")
	    private String language;
}
