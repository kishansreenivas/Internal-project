package com.MovieService.dto;

import com.MovieService.Entity.Theatre;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter

public class ScreenResponseDto {
    private String id;
    private String name;
    private Theatre theatre; // or a TheatreDto if you want to limit fields
}
