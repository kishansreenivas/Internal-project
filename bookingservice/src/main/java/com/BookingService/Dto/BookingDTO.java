package com.BookingService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long movieId;
    private String showTime;
    private int numberOfSeats;
    // Add other fields if required
    
}
