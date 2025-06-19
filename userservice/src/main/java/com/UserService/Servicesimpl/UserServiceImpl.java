package com.UserService.Servicesimpl;

import com.UserService.Dto.UserDTO;
import com.UserService.Entity.User;
import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.External.Service.BookingServiceClient;
import com.UserService.Mapper.UserMapper;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
	@Autowired
    private final UserRepository userRepo;
	@Autowired
    private final UserMapper mapper;
	@Autowired
    private final BookingServiceClient bookingServiceClient;

	@Override
	public UserDTO createUser(UserDTO dto) {
	    log.info("ENTRY: createUser() - DTO: {}", dto);

	    // Manual null check (if needed, depending on validation at controller)
	    if (dto == null) {
	        log.warn("UserDTO is null");
	        throw new IllegalArgumentException("User data cannot be null");
	    }

	    // Map DTO to entity
	    User user = mapper.toEntity(dto);

	    // Ensure ID is set
	    if (user.getId() == null) {
	        user.setId(UUID.randomUUID());
	    }

	    // Save to DB
	    User saved = userRepo.save(user);

	    // Map back to DTO
	    UserDTO result = mapper.toDTO(saved);

	    log.info("EXIT: createUser() - Saved User: {}", result);
	    return result;
	}

    @Override
    public UserDTO updateUser(UUID id, UserDTO dto) {
        log.info("ENTRY: updateUser() - ID: {}, DTO: {}", id, dto);

        User existing = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        mapper.updateEntityFromDto(dto, existing);

        User updated = userRepo.save(existing);
        UserDTO result = mapper.toDTO(updated);

        log.info("EXIT: updateUser() - Updated User: {}", result);
        return result;
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("ENTRY: deleteUser() - ID: {}", id);

        if (!userRepo.existsById(id)) {
            log.warn("User not found with ID: {}", id);
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepo.deleteById(id);
        log.info("EXIT: deleteUser() - Deleted ID: {}", id);
    }

    @Override
    public List<Long> getWatchlist(UUID userId) {
        log.info("ENTRY: getWatchlist() - UserID: {}", userId);

        List<Long> watchlist = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId))
                .getWatchlistMovieIds();

        log.info("EXIT: getWatchlist() - Count: {}", watchlist.size());
        return watchlist;
    }

    @Override
    public void addToWatchlist(UUID userId, Long movieId) {
        log.info("ENTRY: addToWatchlist() - UserID: {}, MovieID: {}", userId, movieId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Long> watchlist = user.getWatchlistMovieIds();
        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
            userRepo.save(user);
            log.info("Movie added to watchlist: {}", movieId);
        } else {
            log.debug("Movie already in watchlist: {}", movieId);
        }
    }

    @Override
    public void removeFromWatchlist(UUID userId, Long movieId) {
        log.info("ENTRY: removeFromWatchlist() - UserID: {}, MovieID: {}", userId, movieId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Long> watchlist = user.getWatchlistMovieIds();
        if (watchlist.contains(movieId)) {
            watchlist.remove(movieId);
            userRepo.save(user);
            log.info("Movie removed from watchlist: {}", movieId);
        } else {
            log.debug("Movie not found in watchlist: {}", movieId);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("ENTRY: getAllUsers()");

        List<User> users = userRepo.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDTO dto = mapper.toDTO(user);
            try {
                UserDTO bookingData = bookingServiceClient.getUserWithBookings(user.getId());
                dto.setBookingId(bookingData.getBookingId());
            } catch (Exception e) {
                log.warn("Booking fetch failed for user {}: {}", user.getId(), e.getMessage());
                dto.setBookingId(new ArrayList<>());
            }
            userDTOs.add(dto);
        }

        log.info("EXIT: getAllUsers() - Count: {}", userDTOs.size());
        return userDTOs;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        log.info("ENTRY: getUserById() - ID: {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for ID: {}", id);
                    return new ResourceNotFoundException("User not found");
                });

        UserDTO userDto = mapper.toDTO(user);

        try {
            UserDTO enriched = bookingServiceClient.getUserWithBookings(id);
            userDto.setBookingId(enriched.getBookingId());
        } catch (Exception e) {
            log.warn("Booking fetch failed for user {}: {}", id, e.getMessage());
            userDto.setBookingId(new ArrayList<>());
        }

        log.info("EXIT: getUserById() - Result: {}", userDto);
        return userDto;
    }
}
