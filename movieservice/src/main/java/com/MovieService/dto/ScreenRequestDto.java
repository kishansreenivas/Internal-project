package com.MovieService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenRequestDto {
	@NotBlank(message = "Screen name is required")
    private String name;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @NotBlank(message = "Theatre ID is required")
    private String theatreId;
}