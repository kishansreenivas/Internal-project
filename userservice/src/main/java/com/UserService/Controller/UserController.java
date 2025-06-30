package com.UserService.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDto;
import com.UserService.Dto.MovieDTO;
import com.UserService.Dto.UserContactDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    @Autowired
    private final UserService userService;

    private static final String USER_SERVICE_CB = "userServiceCircuitBreaker";

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDto) {
        log.info("Creating user: {}", userDto.getEmail());
        UserDTO createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }

    @GetMapping
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "fallbackGetAllUsers")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // Fallback for getAllUsers
    public ResponseEntity<ApiResponse<List<UserDTO>>> fallbackGetAllUsers(Exception ex) {
        log.error("Fallback for getAllUsers: {}", ex.getMessage());
        UserDTO user = new UserDTO();
        user.setId(UUID.randomUUID());
        user.setFirstName("Dummy");
        user.setLastName("User");
        user.setEmail("dummy.user@example.com");
        user.setPhone("0000000000");

        BookingDto fallbackBooking = new BookingDto();
        fallbackBooking.setBookingId("Booking service is down. Unable to fetch bookings.");
        user.setBookings(List.of(fallbackBooking));

        MovieDTO fallbackMovie = new MovieDTO();
        fallbackMovie.setId("N/A");
        fallbackMovie.setTitle("Movie service unavailable");
        user.setWatchlistMovies(List.of(fallbackMovie));

        List<UserDTO> dummyList = List.of(user);
        return ResponseEntity.ok(ApiResponse.success(dummyList));
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "fallbackGetUserById")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        log.info("Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    // Fallback for getUserById
    public ResponseEntity<ApiResponse<UserDTO>> fallbackGetUserById(UUID id, Exception ex) {
        log.error("Fallback for getUserById: {}", ex.getMessage());

        UserDTO user = new UserDTO();
        user.setId(id);
        user.setFirstName("Unavailable");
        user.setLastName("User");
        user.setEmail("unavailable@example.com");
        user.setPhone("N/A");

        BookingDto fallbackBooking = new BookingDto();
        fallbackBooking.setBookingId("Booking service is down. Unable to fetch bookings.");
        user.setBookings(List.of(fallbackBooking));

        MovieDTO fallbackMovie = new MovieDTO();
        fallbackMovie.setId("N/A");
        fallbackMovie.setTitle("Movie service unavailable");
        user.setWatchlistMovies(List.of(fallbackMovie));

        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDto) {
        log.info("Updating user with ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> getWatchlist(@PathVariable UUID id) {
        log.info("Fetching watchlist for user ID: {}", id);
        List<MovieDTO> watchlist = userService.getWatchlist(id);
        return ResponseEntity.ok(ApiResponse.success(watchlist));
    }

    @PostMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> addToWatchlist(@PathVariable UUID id, @RequestParam String movieId) {
        log.info("Adding movie {} to user {} watchlist", movieId, id);
        userService.addToWatchlist(id, movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie added to watchlist"));
    }

    @DeleteMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> removeFromWatchlist(@PathVariable UUID id, @RequestParam String movieId) {
        log.info("Removing movie {} from user {} watchlist", movieId, id);
        userService.removeFromWatchlist(id, movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie removed from watchlist"));
    }

    @GetMapping("/contact/{userId}")
    public ResponseEntity<ApiResponse<UserContactDTO>> getUserContact(@PathVariable UUID userId) {
        log.info("Fetching contact details for user ID: {}", userId);
        UserDTO userDto = userService.getUserById(userId);
        UserContactDTO contactDTO = new UserContactDTO(userDto.getEmail(), userDto.getPhone());
        return ResponseEntity.ok(ApiResponse.success(contactDTO));
    }
}
