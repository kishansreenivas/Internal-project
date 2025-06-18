package com.UserService.Services;

import java.util.List;
import java.util.UUID;

import com.UserService.Dto.UserDTO;

public interface UserService {
	
    UserDTO createUser(UserDTO dto);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID id);
    UserDTO updateUser(UUID id, UserDTO dto);
    void deleteUser(UUID id);
    List<Long> getWatchlist(UUID userId);
    void addToWatchlist(UUID userId, Long movieId);
    void removeFromWatchlist(UUID userId, Long movieId);
}
