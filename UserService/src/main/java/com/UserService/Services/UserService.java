package com.UserService.Services;

import java.util.UUID;
import java.util.prefs.Preferences;

import com.UserService.Dto.UserDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.User;


import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO dto);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);
    List<Long> getWatchlist(Long userId);
    void addToWatchlist(Long userId, Long movieId);
    void removeFromWatchlist(Long userId, Long movieId);
}