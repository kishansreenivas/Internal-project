package com.UserService.Dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDTO {
    private UUID  id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private List<AddressDTO> addresses;
    private PreferencesDTO preferences;
    private List<PaymentMethodDTO> paymentMethods;
    private List<Long> watchlistMovieIds;
    private List<Long> bookingId;
    

   
  
}
