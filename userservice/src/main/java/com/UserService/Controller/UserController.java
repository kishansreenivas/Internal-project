package com.UserService.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDTO;
import com.UserService.Dto.MovieDTO;
import com.UserService.Dto.UserContactDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Services.UserService;
import com.UserService.constants.AppMessages;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @Valid @RequestBody UserDTO userDto,
            HttpServletRequest request,
            HttpServletResponse response) {

        log.info("Creating user: {}", userDto.getEmail());
        UserDTO createdUser = userService.createUser(userDto);

        //  Create new session
        HttpSession session = request.getSession(true);
        session.setAttribute("userEmail", createdUser.getEmail());
        session.setAttribute("userId", createdUser.getId());

        // Create secure cookie with session ID
        Cookie cookie = new Cookie("user_session", session.getId());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1800); // 30 minutes
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("Session and cookie created for user: {}", createdUser.getEmail());

        return ResponseEntity.ok(ApiResponse.success(createdUser, AppMessages.USER_CREATED));
    }


    @GetMapping
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "fallbackGetAllUsers")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = USER_SERVICE_CB, fallbackMethod = "fallbackGetUserById")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        log.info("Fetching user with ID: {}", id);
        UserDTO user = userService.getUserById(id);
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
        return ResponseEntity.ok(ApiResponse.success(AppMessages.USER_DELETED));
    }

    @GetMapping("/watchlists")
    public ResponseEntity<ApiResponse<Map<UUID, List<MovieDTO>>>> getAllWatchlists() {
        Map<UUID, List<MovieDTO>> allWatchlists = userService.getAllWatchlists();
        return ResponseEntity.ok(ApiResponse.success(allWatchlists));
    }

    @GetMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> getWatchlist(@PathVariable UUID id) {
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

 //  Find by email
    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(ApiResponse.success(user, "User found")))
                .orElse(ResponseEntity.status(404).body(ApiResponse.failure("User not found with email: " + email)));
    }

    //  Find users by city
    @GetMapping("/city")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByCity(@RequestParam String city) {
        List<UserDTO> users = userService.getUsersByCity(city);
        if (users.isEmpty()) {
            return ResponseEntity.status(404).body(ApiResponse.failure("No users found in city: " + city));
        }
        return ResponseEntity.ok(ApiResponse.success(users, "Users from city: " + city));
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

        BookingDTO fallbackBooking = new BookingDTO();
        fallbackBooking.setBookingId("Booking service is down. Unable to fetch bookings.");
        user.setBookings(List.of(fallbackBooking));

        MovieDTO fallbackMovie = new MovieDTO();
        fallbackMovie.setId("N/A");
        fallbackMovie.setTitle("Movie service unavailable");
        user.setWatchlistMovies(List.of(fallbackMovie));

        List<UserDTO> dummyList = List.of(user);
        return ResponseEntity.ok(ApiResponse.success(dummyList));
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

        BookingDTO fallbackBooking = new BookingDTO();
        fallbackBooking.setBookingId("Booking service is down. Unable to fetch bookings.");
        user.setBookings(List.of(fallbackBooking));

        MovieDTO fallbackMovie = new MovieDTO();
        fallbackMovie.setId("N/A");
        fallbackMovie.setTitle("Movie service unavailable");
        user.setWatchlistMovies(List.of(fallbackMovie));

        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> loginUser(
            @RequestBody Map<String, String> loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        return userService.getUserByUsername(username).map(user -> {
            if (user.getPassword().equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("userEmail", user.getEmail());
                session.setAttribute("userId", user.getId());

                Cookie cookie = new Cookie("user_session", session.getId());
                cookie.setHttpOnly(true);
                cookie.setMaxAge(1800);
                cookie.setPath("/");
                response.addCookie(cookie);

                //  Explicitly type the ApiResponse
                ApiResponse<String> responseBody = ApiResponse.success("Login successful. Session and cookie set.");
                return ResponseEntity.ok(responseBody);
            } else {
                ApiResponse<String> errorResponse = ApiResponse.failure("Invalid username or password");
                return ResponseEntity.status(401).body(errorResponse);
            }
        }).orElseGet(() -> {
            ApiResponse<String> errorResponse = ApiResponse.failure("User not found");
            return ResponseEntity.status(401).body(errorResponse);
        });
    }


    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("user_session", null);
        cookie.setMaxAge(0); // delete cookie
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("User logged out. Session invalidated.");

        //  Explicitly assign response with correct type
        ApiResponse<String> responseBody = ApiResponse.success("User logged out successfully");
        return ResponseEntity.ok(responseBody);
    }

}
