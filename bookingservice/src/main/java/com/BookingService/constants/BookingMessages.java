package com.BookingService.constants;

public class BookingMessages {

    // ✅ Success Messages
    public static final String BOOKING_INITIATED_SUCCESS = "Booking initiated successfully";
    public static final String BOOKING_CONFIRMED_SUCCESS = "Booking confirmed";
    public static final String BOOKING_CANCELLED_SUCCESS = "Booking cancelled";
    public static final String SEATS_FETCHED_SUCCESS = "Fetched seats";
    public static final String ALL_BOOKED_SEATS_FETCHED = "All booked seats fetched";
    public static final String BOOKINGS_WITH_SEATS_FETCHED = "Bookings with seats fetched";
    public static final String LOCKS_RELEASED = "Expired seat locks released";
    public static final String PAGINATED_BOOKINGS_FETCHED = "Paginated bookings fetched";
    public static final String SCREEN_DETAILS_FETCHED = "Screen details fetched";
    public static final String SHOWTIME_DETAILS_FETCHED = "Showtime details fetched";
    public static final String MAPPED_BOOKING_RESPONSE = "Mapped booking response";
    public static final String USER_PROFILE_FETCHED = "User profile fetched";
    public static final String USER_BOOKINGS_FETCHED = "Bookings fetched";

    // ❌ Error Messages
    public static final String BOOKING_INITIATION_FAILED = "Failed to initiate booking";
    public static final String BOOKING_CONFIRMATION_FAILED = "Booking confirmation failed";
    public static final String BOOKING_CANCELLATION_FAILED = "Failed to cancel booking";
    public static final String SEATS_FETCH_FAILED = "Failed to get seats";
    public static final String ALL_BOOKED_SEATS_FETCH_FAILED = "Failed to get booked seats";
    public static final String BOOKINGS_WITH_SEATS_FETCH_FAILED = "Failed to fetch bookings";
    public static final String LOCKS_RELEASE_FAILED = "Failed to release locks";
    public static final String PAGINATED_BOOKINGS_FETCH_FAILED = "Failed to fetch bookings";
    public static final String SCREEN_FETCH_FAILED = "Failed to fetch screen";
    public static final String SHOWTIME_FETCH_FAILED = "Failed to fetch showtime";
    public static final String MAPPING_FAILED = "Mapping failed";
    public static final String USER_PROFILE_FETCH_FAILED = "Failed to fetch user";
    public static final String USER_BOOKINGS_FETCH_FAILED = "Failed to get bookings";

    // Prevent instantiation
    private BookingMessages() {}
}
