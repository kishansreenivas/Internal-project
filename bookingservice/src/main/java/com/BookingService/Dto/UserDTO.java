package com.BookingService.Dto;


import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Long> bookingIds;

    
}
