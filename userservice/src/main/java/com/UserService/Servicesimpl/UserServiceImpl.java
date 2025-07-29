package com.UserService.Servicesimpl;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.UserService.Dto.AddressDTO;
import com.UserService.Dto.ApiResponse;
import com.UserService.Dto.BookingDTO;
import com.UserService.Dto.MovieDto;
import com.UserService.Dto.UserDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;

import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.External.Service.BookingServiceClient;
import com.UserService.External.Service.MovieServiceClient;
import com.UserService.Mapper.AddressMapper;
import com.UserService.Mapper.UserMapper;
import com.UserService.Repositories.AddressRepository;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.UserService;
import com.UserService.constants.AppMessages;
import com.UserService.constants.BookingStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService, Serializable {

    @Autowired private UserRepository userRepo;
    @Autowired private UserMapper mapper;
    @Autowired private BookingServiceClient bookingServiceClient;
    @Autowired private MovieServiceClient movieServiceClient;
    @Autowired private AddressServiceImpl addressService;
    @Autowired private AddressRepository addressRepository;
    @Autowired private ModelMapper modelMapper;
    @Autowired private AddressMapper addressMapper;
    @Autowired private BCryptPasswordEncoder encoder;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    @Override
    public List<UserDTO> getAllUsers() {
    	  log.info(AppMessages.ENTRY_LOG_MESSAGE, "getAllUsers");

        List<User> users = userRepo.findAll();  
        List<UserDTO> userDTOs = new ArrayList<>(); //empty list he

        for (User user : users) {
            UserDTO dto = mapper.toDTO(user);

            try {
                ApiResponse<List<BookingDTO>> response = bookingServiceClient.getBookingsByUser(user.getId().toString());
                if (response != null && response.getData() != null) {
                    dto.setBookings(response.getData());
                } else {
                	log.warn(AppMessages.NO_BOOKINGS_FOUND_LOG_MESSAGE, user.getId());
                    dto.setBookings(Collections.emptyList());
                }
            } catch (Exception e) {
                log.error("Failed to fetch bookings for user {}: {}", user.getId(), e.getMessage());
                dto.setBookings(List.of(getDummyBookingDto(AppMessages.BOOKING_SERVICE_ERROR_MESSAGE)));
            }

            try {
                List<MovieDto> movieDetails = new ArrayList<>();
                if (user.getWatchlistMovieIds() != null) {
                    for (String movieId : user.getWatchlistMovieIds()) {
                        try {
                            ApiResponse<MovieDto> movieResponse = movieServiceClient.getMovieById(movieId);
                            if (movieResponse != null && movieResponse.isSuccess() && movieResponse.getData() != null) {
                                movieDetails.add(movieResponse.getData());
                            }
                        } catch (Exception ex) {
                            log.error(AppMessages.NO_MOVIES_FOUND_LOG_MESSAGE, movieId, user.getId(), ex.getMessage());
                        }
                    }
                }
                dto.setWatchlistMovies(movieDetails);
            } catch (Exception ex) {
                log.error(AppMessages.MOVIE_SERVICE_ERROR_LOG_MESSAGE, user.getId(), ex.getMessage());
                dto.setWatchlistMovies(Collections.emptyList());
            }

            userDTOs.add(dto);
        }

        log.info("EXIT: getAllUsers() - Count: {}", userDTOs.size());
        return userDTOs;
    }

    @Override
    public UserDTO getUserById(UUID id) {
        log.info(AppMessages.ENTRY_LOG_MESSAGE, id);

        User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn(AppMessages.USER_NOT_FOUND_LOG_MESSAGE, id);
                    return new ResourceNotFoundException("User not found");
                });

        UserDTO userDto = mapper.toDTO(user);

        try {
            ApiResponse<List<BookingDTO>> response = bookingServiceClient.getBookingsByUser(id.toString());
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

        try {
            List<MovieDto> movieDetails = new ArrayList<>();
            if (user.getWatchlistMovieIds() != null) {
                for (String movieId : user.getWatchlistMovieIds()) {
                    try {
                        ApiResponse<MovieDto> movieResponse = movieServiceClient.getMovieById(movieId);
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


    @Override
    public UserDTO createUser(UserDTO dto) {
        log.info("ENTRY: createUser() - DTO: {}", dto);

        if (dto == null) {
            log.warn("UserDTO is null");
            throw new IllegalArgumentException("User data cannot be null");
        }

        try {
            // Reflection Example: Inspect methods of UserDTO
            Method[] methods = dto.getClass().getDeclaredMethods();
            for (Method method : methods) {
                log.debug("UserDTO method: {}", method.getName());
            }

            // File I/O 
            Path userDir = Paths.get("C://Documents//Simple//SpringBoot//File//createusers.txt");
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            FileWriter writer = new FileWriter(userDir.resolve("C://Documents//Simple//SpringBoot//File//createuser.txt").toFile(), true);
            writer.write("username: " + dto.getEmail() + System.lineSeparator());
            writer.close();

            User user = mapper.toEntity(dto);
            if (user.getId() == null) {
                user.setId(UUID.randomUUID());
            }
            user.setPassword(encoder.encode(user.getPassword())); // ← Added password encryption here
            User saved = userRepo.save(user);

            // Concurrency: Log async without blocking
            CompletableFuture.runAsync(() -> log.debug("Async log: user {} created", saved.getId()), executorService);

            UserDTO result = mapper.toDTO(saved);

            log.info("EXIT: createUser() - Saved User: {}", result);
            return result;
        } catch (IOException e) {
            log.error("I/O Error while creating user: {}", e.getMessage());
            throw new RuntimeException("Unable to log user creation", e);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new RuntimeException("Unexpected error during user creation", e);
        }
    }

    @Override
    @Transactional
    public UserDTO updateUser(UUID id, UserDTO dto) {
        log.info("ENTRY: updateUser() - ID: {}, DTO: {}", id, dto);

        User existing = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setUsername(dto.getEmail());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setPassword(encoder.encode(dto.getPassword()));

        existing.getAddresses().clear();

        if (dto.getAddresses() != null && !dto.getAddresses().isEmpty()) {
            for (AddressDTO addressDto : dto.getAddresses()) {
                Address addressEntity = addressMapper.toEntity(addressDto, existing);
                addressEntity.setUser(existing);
                existing.getAddresses().add(addressEntity);
            }
        }

        User savedUser = userRepo.save(existing);
        UserDTO result = mapper.toDTO(savedUser);

        log.info("EXIT: updateUser() - Updated User: {}", result);
        return result;
    }

    @Override
    public void deleteUser(UUID id) {
        log.info("ENTRY: deleteUser() - ID: {}", id);
        User user = userRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", id);
                    throw new ResourceNotFoundException("User not found with id: " + id);
                });

        userRepo.delete(user);
        log.info("EXIT: deleteUser() - Deleted ID: {}", id);
    }


    @Override
    public Map<UUID, List<MovieDto>> getAllWatchlists() {
        log.info("Fetching detailed watchlists for users who booked movies");

        List<User> users = userRepo.findAll();
        Map<UUID, List<MovieDto>> watchlistMap = new HashMap<>();

        for (User user : users) {
            List<String> movieIds = user.getWatchlistMovieIds(); // or similar field
            if (movieIds != null && !movieIds.isEmpty()) {
                List<MovieDto> movies = new ArrayList<>();
                for (String movieId : movieIds) {
                    try {
                        ApiResponse<MovieDto> movieResp = movieServiceClient.getMovieById(movieId);
                        if (movieResp != null && movieResp.isSuccess() && movieResp.getData() != null) {
                            movies.add(movieResp.getData());
                        }
                    } catch (Exception e) {
                        log.error("Error fetching movie {} for user {}: {}", movieId, user.getId(), e.getMessage());
                    }
                }
                if (!movies.isEmpty()) {
                    watchlistMap.put(user.getId(), movies);
                    log.debug("User {}: {} movies added to watchlist", user.getId(), movies.size());
                }
            }
        }

        log.info("Completed fetching all user watchlists");
        return watchlistMap;
    }


    @Override
    public List<MovieDto> getWatchlist(UUID userId) {
        log.info("ENTRY: getWatchlist() - UserID: {}", userId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<String> movieIds = user.getWatchlistMovieIds();
        List<MovieDto> watchlistMovies = new ArrayList<>();

        if (movieIds != null && !movieIds.isEmpty()) {
            for (String movieId : movieIds) {
                try {
                    ApiResponse<MovieDto> response = movieServiceClient.getMovieById(movieId);
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        watchlistMovies.add(response.getData());
                    } else {
                        log.warn("Empty or failed movie data for ID: {}", movieId);
                    }
                } catch (Exception ex) {
                    log.error("Failed to fetch movie {}: {}", movieId, ex.getMessage());
                }
            }
        } else {
            log.warn("Watchlist is empty or null for user: {}", userId);
        }

        log.info("EXIT: getWatchlist() - Movies fetched: {}", watchlistMovies.size());
        return watchlistMovies;
    }

    @Override
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
            user.setWatchlistMovieIds(watchlist);
            userRepo.save(user);
            log.info("Movie added to watchlist: {}", movieId);
        } else {
            log.debug("Movie already in watchlist: {}", movieId);
        }

        log.info("EXIT: addToWatchlist() - Updated watchlist size: {}", watchlist.size());
    }

    @Override
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
    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("Looking up user by email: {}", email);
        Optional<UserDTO> result = userRepo.findByEmail(email)
                                           .map(mapper::toDTO);

        if (result.isEmpty()) {
            log.warn("No user found with email: {}", email);
            throw new ResourceNotFoundException("User not found for email: " + email);
        }

        log.info("User found for email: {}", email);
        return result;
    }

    @Override
    public List<UserDTO> getUsersByCity(String city) {
        log.info("Fetching users by city: {}", city);
        List<UserDTO> users = userRepo.findUsersByCity(city).stream()
                                      .map(mapper::toDTO)
                                      .collect(Collectors.toList());

        if (users.isEmpty()) {
            log.warn("No users found in city: {}", city);
            throw new ResourceNotFoundException("No users found in city: " + city);
        }

        log.info("Found {} user(s) in city: {}", users.size(), city);
        return users;
    }

    @Override
    public Optional<UserDTO> getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        return userRepo.findByUsername(username)
                             .map(mapper::toDTO);
    }

    private BookingDTO getDummyBookingDto(String message) {
        BookingDTO dummy = new BookingDTO();
        dummy.setBookingId(message);
        dummy.setShowId("N/A");
        dummy.setScreenId("N/A");
        dummy.setBookingTime(null);
        dummy.setPaymentId("N/A");
        dummy.setTotalAmount(0.0);
        dummy.setStatus(BookingStatus.CANCELLED);
        return dummy;
    }

    public void inspectUserFields() {
        try {
            Field[] fields = User.class.getDeclaredFields();
            for (Field field : fields) {
                log.debug("User field: {} of type {}", field.getName(), field.getType());
            }
        } catch (Exception e) {
            log.error("Reflection failed", e);
        }
    }

    public void writeUserInfoToFile(UUID userId, String directory) {
        try {
            Path path = Paths.get(directory + "/" + userId + "_info.txt");
            Files.writeString(path, "User exported at: " + Instant.now());
            log.info("User info written to file: {}", path);
        } catch (Exception e) {
            log.error("Failed to write user info", e);
        }
    }

    public void fetchUsersConcurrently() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CompletableFuture.runAsync(() -> {
            List<UserDTO> allUsers = getAllUsers();
            log.info("Async fetched {} users", allUsers.size());
        }, executor);
    }

    public boolean isServiceReachable(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (Exception e) {
            log.warn("Service {}:{} not reachable", host, port);
            return false;
        }
    }
}
