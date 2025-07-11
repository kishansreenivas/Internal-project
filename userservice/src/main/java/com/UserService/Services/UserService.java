package com.UserService.Services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.UserService.Dto.MovieDTO;
import com.UserService.Dto.UserDTO;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID id);
    UserDTO updateUser(UUID id, UserDTO dto);
    void deleteUser(UUID id);
    List<MovieDTO> getWatchlist(UUID userId);
    void addToWatchlist(UUID userId, String movieId);
    void removeFromWatchlist(UUID userId, String movieId);
    Optional<UserDTO> getUserByEmail(String email);
    Optional<UserDTO> getUserByUsername(String username);
    List<UserDTO> getUsersByCity(String city);
    Map<UUID, List<MovieDTO>> getAllWatchlists();

 
}
