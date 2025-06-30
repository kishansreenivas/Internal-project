package com.UserService.Servicesimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDto;
import com.UserService.Dto.MovieDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Entity.User;
import com.UserService.Enum.BookingStatus;
import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.External.Service.BookingServiceClient;
import com.UserService.External.Service.MovieServiceClient;
import com.UserService.Mapper.UserMapper;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	@Autowired
    private final MovieServiceClient movieServiceClient;
	
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
   // @CacheEvict(value = "users", key = "#id")
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
   // @CacheEvict(value = "users", key = "#id")
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
    public List<MovieDTO> getWatchlist(UUID userId) {
        log.info("ENTRY: getWatchlist() - UserID: {}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> movieIds = user.getWatchlistMovieIds();
        List<MovieDTO> watchlistMovies = new ArrayList<>();

        if (movieIds != null) {
            for (String movieId : movieIds) {
                try {
                    ApiResponse<MovieDTO> movieResponse = movieServiceClient.getMovieById(movieId);
                    if (movieResponse != null && movieResponse.isSuccess() && movieResponse.getData() != null) {
                        watchlistMovies.add(movieResponse.getData());
                    } else {
                        log.warn("No movie data found for ID: {}", movieId);
                    }
                } catch (Exception e) {
                    log.error("Failed to fetch movie details for ID: {} - {}", movieId, e.getMessage());
                }
            }
        } else {
            log.warn("Watchlist is null for user: {}", userId);
        }

        log.info("EXIT: getWatchlist() - Total movies fetched: {}", watchlistMovies.size());
        return watchlistMovies;
    }


    @Override
    //@CacheEvict(value = "watchlists", key = "#userId")
    public void addToWatchlist(UUID userId, String movieId) {
        log.info("ENTRY: addToWatchlist() - UserID: {}, MovieID: {}", userId, movieId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> watchlist = user.getWatchlistMovieIds();

        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }

        if (!watchlist.contains(movieId)) {
            watchlist.add(movieId);
            user.setWatchlistMovieIds(watchlist); // ensure persistence
            userRepo.save(user); // persist change
            log.info("Movie added to watchlist: {}", movieId);
        } else {
            log.debug("Movie already in watchlist: {}", movieId);
        }

        log.info("EXIT: addToWatchlist() - Updated watchlist size: {}", watchlist.size());
    }


    @Override
   // @CacheEvict(value = "watchlists", key = "#userId")
    public void removeFromWatchlist(UUID userId, String movieId) {
        log.info("ENTRY: removeFromWatchlist() - UserID: {}, MovieID: {}", userId, movieId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> watchlist = user.getWatchlistMovieIds();
        if (watchlist.contains(movieId)) {
            watchlist.remove(movieId);
            userRepo.save(user);
            log.info("Movie removed from watchlist: {}", movieId);
        } else {
            log.debug("Movie not found in watchlist: {}", movieId);
        }
    }

    @Override
   // @Cacheable(value = "users")
    public List<UserDTO> getAllUsers() {
        log.info("ENTRY: getAllUsers()");

        List<User> users = userRepo.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserDTO dto = mapper.toDTO(user);

            // Fetch bookings
            try {
                ApiResponse<List<BookingDto>> response = bookingServiceClient.getBookingsByUser(user.getId().toString());

                if (response != null && response.getData() != null) {
                    dto.setBookings(response.getData());
                } else {
                    log.warn("No bookings found for user {}", user.getId());
                    dto.setBookings(Collections.emptyList());
                }
            } catch (Exception e) {
                log.error("Failed to fetch bookings for user {}: {}", user.getId(), e.getMessage());
                dto.setBookings(List.of(getDummyBookingDto("Booking service is down. Unable to fetch bookings.")));
            }

            // Fetch watchlist movie details
            try {
                List<MovieDTO> movieDetails = new ArrayList<>();
                if (user.getWatchlistMovieIds() != null) {
                    for (String movieId : user.getWatchlistMovieIds()) {
                        try {
                            ApiResponse<MovieDTO> movieResponse = movieServiceClient.getMovieById(movieId);
                            if (movieResponse != null && movieResponse.isSuccess() && movieResponse.getData() != null) {
                                movieDetails.add(movieResponse.getData());
                            }
                        } catch (Exception ex) {
                            log.error("Failed to fetch movie {} for user {}: {}", movieId, user.getId(), ex.getMessage());
                        }
                    }
                }
                dto.setWatchlistMovies(movieDetails);
            } catch (Exception ex) {
                log.error("MovieService down for user {}: {}", user.getId(), ex.getMessage());
                dto.setWatchlistMovies(Collections.emptyList());
            }

            userDTOs.add(dto);
        }

        log.info("EXIT: getAllUsers() - Count: {}", userDTOs.size());
        return userDTOs;
    }


    @Override
    //@Cacheable(value = "users", key = "#id")
    public UserDTO getUserById(UUID id) {
        log.info("ENTRY: getUserById() - ID: {}", id);

        User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found for ID: {}", id);
                    return new ResourceNotFoundException("User not found");
                });

        UserDTO userDto = mapper.toDTO(user);

        try {
            ApiResponse<List<BookingDto>> response = bookingServiceClient.getBookingsByUser(id.toString());

            if (response != null && response.getData() != null) {
                userDto.setBookings(response.getData());
                log.info("Fetched {} bookings for user {}", response.getData().size(), id);
            } else {
                log.warn("No bookings returned for user {}", id);
                userDto.setBookings(Collections.emptyList());
            }
        } catch (Exception e) {
            log.error("Failed to fetch bookings for user {}: {}", id, e.getMessage());
            userDto.setBookings(List.of(getDummyBookingDto("Booking service is down. Unable to fetch bookings.")));
        }

        // Watchlist Movies Fetch Logic
        try {
            List<MovieDTO> movieDetails = new ArrayList<>();
            if (user.getWatchlistMovieIds() != null) {
                for (String movieId : user.getWatchlistMovieIds()) {
                    try {
                        ApiResponse<MovieDTO> movieResponse = movieServiceClient.getMovieById(movieId);
                        if (movieResponse != null && movieResponse.isSuccess() && movieResponse.getData() != null) {
                            movieDetails.add(movieResponse.getData());
                        }
                    } catch (Exception ex) {
                        log.error("Failed to fetch movie {}: {}", movieId, ex.getMessage());
                    }
                }
            }
            userDto.setWatchlistMovies(movieDetails);
        } catch (Exception ex) {
            log.error("MovieService down for user {}: {}", id, ex.getMessage());
            userDto.setWatchlistMovies(Collections.emptyList());
        }

        log.info("EXIT: getUserById() - Result: {}", userDto);
        return userDto;
    }


    private BookingDto getDummyBookingDto(String message) {
        BookingDto dummy = new BookingDto();
        dummy.setBookingId(message);
        dummy.setShowId("N/A");
        dummy.setScreenId("N/A");
        dummy.setBookingTime(null);
        dummy.setPaymentId("N/A");
        dummy.setTotalAmount(0.0);
        dummy.setStatus(BookingStatus.CANCELLED);
        return dummy;
    }

}
