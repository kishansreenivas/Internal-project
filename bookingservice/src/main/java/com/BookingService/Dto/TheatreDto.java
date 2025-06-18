package com.BookingService.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreDto {
    private String id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String pincode;
}
