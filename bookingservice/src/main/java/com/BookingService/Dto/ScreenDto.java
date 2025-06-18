package com.BookingService.Dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDto {
    private String id;
    private String name;
    private Integer totalSeats;
   // private TheatreDto theatre; // You can create a TheatreDto too if needed
}
