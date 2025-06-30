package com.UserService.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be valid")
    private String phone;

    private List<AddressDTO> addresses;

    private List<PaymentMethodDTO> paymentMethods;

    // ✅ Movie IDs in user's watchlist
    private List<String> watchlistMovieIds = new ArrayList<>();

    // ✅ Movie details (populated via Feign)
    private List<MovieDTO> watchlistMovies = new ArrayList<>();

    // ✅ Booking details
    private List<BookingDto> bookings = new ArrayList<>();
}
