package com.UserService.Services;

import java.util.List;
import java.util.UUID;
import java.util.prefs.Preferences;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UserService.Config.UserMapper;
import com.UserService.Dto.UserDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.User;
import com.UserService.Repositories.UserRepository;

import Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    public UserDTO createUser(UserDTO dto) {
        User user = mapper.toEntity(dto);
        return mapper.toDTO(userRepo.save(user));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException ("User not found with id: " + id));
        return mapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User existing = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDTO(userRepo.save(existing));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }

    @Override
    public List<Long> getWatchlist(Long userId) {
        return userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId))
            .getWatchlistMovieIds();
    }

    @Override
    public void addToWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Long> watchlist = user.getWatchlistMovieIds();
        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
            userRepo.save(user);
        }
    }

    @Override
    public void removeFromWatchlist(Long userId, Long movieId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Long> watchlist = user.getWatchlistMovieIds();
        if (watchlist.contains(movieId)) {
            watchlist.remove(movieId);
            userRepo.save(user);
        }
    }
}