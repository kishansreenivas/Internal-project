package com.BookingService.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
	private String userId;
    private String showId;
    private String screenId;
    private List<String> seatIds;
    private double totalAmount;
}

