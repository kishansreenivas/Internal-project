package com.MovieService.dto;

import com.MovieService.Entity.Theatre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ScreenResponseDto {
    private String id;

    @NotBlank(message = "Screen name is required")
    private String name;

    @NotNull(message = "Theatre is required")
    private Theatre theatre; 
}
