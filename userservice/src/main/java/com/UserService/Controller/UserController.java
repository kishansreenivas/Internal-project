package com.UserService.Controller;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.UserContactDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Services.UserService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDto) {
        log.info("Creating user: {}", userDto.getEmail());
        UserDTO createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
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
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<List<Long>>> getWatchlist(@PathVariable UUID id) {
        log.info("Fetching watchlist for user ID: {}", id);
        List<Long> watchlist = userService.getWatchlist(id);
        return ResponseEntity.ok(ApiResponse.success(watchlist));
    }

    @PostMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> addToWatchlist(@PathVariable UUID id, @RequestParam Long movieId) {
        log.info("Adding movie {} to user {} watchlist", movieId, id);
        userService.addToWatchlist(id, movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie added to watchlist"));
    }

    @DeleteMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> removeFromWatchlist(@PathVariable UUID id, @RequestParam Long movieId) {
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
