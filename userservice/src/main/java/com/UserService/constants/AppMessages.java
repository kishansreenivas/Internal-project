package com.UserService.constants;


public class AppMessages {
    public static final String USER_CREATED = "User created and session initialized.";
    public static final String USER_DELETED = "User and Users Address deleted successfully";
    public static final String LOGIN_SUCCESS = "Login successful. Session and cookie set.";
    public static final String LOGIN_FAILED = "Invalid username or password";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_FOUND = "User found";
    public static final String USERS_FOUND_BY_CITY = "Users from city: ";
    public static final String NO_USERS_IN_CITY = "No users found in city: ";
    public static final String MOVIE_ADDED_TO_WATCHLIST = "Movie added to watchlist";
    public static final String MOVIE_REMOVED_FROM_WATCHLIST = "Movie removed from watchlist";
    public static final String USER_LOGOUT_SUCCESS = "User logged out successfully";
    
    public static final String ENTRY_LOG_MESSAGE = "ENTRY: {}()";
    public static final String EXIT_LOG_MESSAGE = "EXIT: {}() - Count: {}";
    public static final String BOOKING_SERVICE_ERROR_MESSAGE = "Booking service is down. Unable to fetch bookings.";
    public static final String MOVIE_SERVICE_ERROR_MESSAGE = "Movie service is down. Unable to fetch movie details.";
    public static final String NO_BOOKINGS_FOUND_LOG_MESSAGE = "No bookings found for user {}";
    public static final String NO_BOOKINGS_RETURNED_LOG_MESSAGE = "No bookings returned for user {}";
    public static final String NO_MOVIES_FOUND_LOG_MESSAGE = "Failed to fetch movie {} for user {}: {}";
    public static final String MOVIE_SERVICE_ERROR_LOG_MESSAGE = "MovieService down for user {}: {}";
    public static final String USER_NOT_FOUND_LOG_MESSAGE = "User not found for ID: {}";
    public static final String USER_NOT_FOUND_EMAIL_LOG_MESSAGE = "No user found with email: {}";
    public static final String MOVIE_NOT_FOUND_LOG_MESSAGE = "Failed to fetch movie {}: {}";
}
