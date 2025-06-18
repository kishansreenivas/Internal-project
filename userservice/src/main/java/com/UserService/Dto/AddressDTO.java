package com.UserService.Dto;

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
public class AddressDTO {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    
 
    private UUID userId;
  
  
}