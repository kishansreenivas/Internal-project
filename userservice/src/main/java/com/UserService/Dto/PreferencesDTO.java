package com.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PreferencesDTO {
    private UUID id;
    private boolean newsletterSubscribed;
    private String language;
    private String region;
}
