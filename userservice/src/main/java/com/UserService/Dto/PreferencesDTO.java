package com.UserService.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PreferencesDTO {
    private UUID id;
    private boolean newsletterSubscribed;
    @NotBlank(message = "Language is required")
    @Size(max = 10, message = "Language must be at most 10 characters")
    private String language;

    @NotBlank(message = "Region is required")
    @Size(max = 10, message = "Region must be at most 10 characters")
    private String region;

}
