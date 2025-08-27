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
    
    /**
     * Retrieves all users and their associated data, including bookings and watchlist movies.
     * <p>
     * This method fetches all users from the repository, converts them to {@link UserDTO} objects, 
     * and attempts to enrich each user's data with their bookings and watchlist movies by calling 
     * external services (booking and movie services). If no bookings or movies are found, 
     * default values are provided. If an error occurs while fetching bookings or movies, 
     * the method logs the error and sets default or empty values accordingly.
     * </p>
     *
     * @return A list of {@link UserDTO} objects, each containing user data along with their associated 
     *         bookings and watchlist movies.
     * @throws None. This method does not throw any checked exceptions, but may log errors if external 
     *         service calls fail.
     */
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

    /**
     * Retrieves a user by their ID and enriches the user data with their associated bookings and watchlist movies.
     * <p>
     * This method fetches a user by their unique ID from the repository and converts it to a {@link UserDTO}. 
     * It then attempts to fetch the user's bookings and watchlist movies from external services. If bookings or 
     * movies are not found, or if any error occurs during the external service calls, the method logs the error 
     * and returns default or empty values. The final {@link UserDTO} is then returned.
     * </p>
     *
     * @param id The unique ID of the user to retrieve.
     * @return   A {@link UserDTO} object containing the user's data, along with their associated bookings and watchlist movies.
     * @throws ResourceNotFoundException If the user with the specified ID does not exist in the repository.
     */
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

    /**
     * Creates a new user based on the provided {@link UserDTO} and persists it to the repository.
     * <p>
     * This method validates the provided {@link UserDTO}, logs its details, performs file I/O to log user creation,
     * and saves the user entity after encoding the password. The method also logs method execution asynchronously 
     * without blocking the main flow. In case of any exceptions during the process, an appropriate error message 
     * is logged, and a runtime exception is thrown.
     * </p>
     *
     * @param dto The {@link UserDTO} object containing the user's details to be created.
     * @return    The saved {@link UserDTO} object after the user has been created and persisted.
     * @throws IllegalArgumentException If the provided {@link UserDTO} is null.
     * @throws RuntimeException If there is an I/O error during logging or any other unexpected error during user creation.
     */
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
            Path userDir = Paths.get("C://Users//167572//OneDrive - Arrow Electronics, Inc//Documents//Simple//springboot//File//createusers.txt");
            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }
            FileWriter writer = new FileWriter(userDir.resolve("C://Users//167572//OneDrive - Arrow Electronics, Inc//Documents//Simple//springboot//File//createuser.txt").toFile(), true);
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

    /**
     * Updates an existing user's details based on the provided {@link UserDTO}.
     * <p>
     * This method retrieves the user by their unique ID, updates the user's personal details 
     * (name, email, phone, and password), and clears the existing addresses before adding the new ones 
     * from the provided DTO. The updated user is then saved and returned as a {@link UserDTO}.
     * The method is wrapped in a transaction to ensure atomicity of the update process.
     * </p>
     *
     * @param id  The unique ID of the user to be updated.
     * @param dto The {@link UserDTO} object containing the updated user details.
     * @return    The updated {@link UserDTO} object.
     * @throws ResourceNotFoundException If the user with the specified ID does not exist.
     * @throws IllegalArgumentException If the provided {@link UserDTO} contains invalid data (e.g., null fields).
     */
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
    
    /**
     * Deletes an existing user based on the provided user ID.
     * <p>
     * This method retrieves the user by their unique ID and deletes the user from the repository. 
     * If the user with the specified ID is not found, a {@link ResourceNotFoundException} is thrown.
     * </p>
     *
     * @param id The unique ID of the user to be deleted.
     * @throws ResourceNotFoundException If the user with the specified ID does not exist.
     */
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

}
