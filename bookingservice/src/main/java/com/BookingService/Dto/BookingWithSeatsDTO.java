package com.BookingService.Dto;

import java.util.List;

import com.BookingService.Entities.BookedSeat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingWithSeatsDTO {
    private String bookingId;
    private List<BookedSeat> seats;

}