package com.UserService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.UserContactDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDto) {
        UserDTO createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDto) {
        UserDTO updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<List<Long>>> getWatchlist(@PathVariable UUID id) {
        List<Long> watchlist = userService.getWatchlist(id);
        return ResponseEntity.ok(ApiResponse.success(watchlist));
    }

    @PostMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> addToWatchlist(@PathVariable UUID id, @RequestParam Long movieId) {
        userService.addToWatchlist(id, movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie added to watchlist"));
    }

    @DeleteMapping("/watchlist/{id}")
    public ResponseEntity<ApiResponse<String>> removeFromWatchlist(@PathVariable UUID id, @RequestParam Long movieId) {
        userService.removeFromWatchlist(id, movieId);
        return ResponseEntity.ok(ApiResponse.success("Movie removed from watchlist"));
    }

    @GetMapping("/contact/{userId}")
    public ResponseEntity<ApiResponse<UserContactDTO>> getUserContact(@PathVariable UUID userId) {
        UserDTO userDto = userService.getUserById(userId);
        UserContactDTO contactDTO = new UserContactDTO(userDto.getEmail(), userDto.getPhone());
        return ResponseEntity.ok(ApiResponse.success(contactDTO));
    }
}
