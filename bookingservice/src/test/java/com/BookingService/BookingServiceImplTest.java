package com.BookingService;

import com.BookingService.Entities.*;
import com.BookingService.Enum.BookingStatus;
import com.BookingService.Exception.SeatAlreadyBookedException;
import com.BookingService.Repositories.BookedSeatRepository;
import com.BookingService.Repositories.BookingRepository;
import com.BookingService.Service.Impl.BookingServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepo;

    @Mock
    private BookedSeatRepository seatRepo;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    //  Test for initiateBooking()
    @Test
    void testInitiateBooking_Success() {
        String userId = "user123";
        String showId = "show001";
        String screenId = "screen123";
        List<String> seatIds = List.of("A1", "A2");
        double totalAmount = 500.0;

        when(seatRepo.existsBySeatIdAndStatusIn(anyString(), anyList())).thenReturn(false);
        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.initiateBooking(userId, showId, screenId, seatIds, totalAmount);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals(2, result.getSeats().size());

        verify(bookingRepo, times(1)).save(any(Booking.class));
    }

    @Test
    void testInitiateBooking_SeatAlreadyBooked_ThrowsException() {
        String userId = "user123";
        String showId = "show001";
        String screenId = "screen123";
        List<String> seatIds = List.of("A1", "A2");
        double totalAmount = 500.0;

        when(seatRepo.existsBySeatIdAndStatusIn(eq("A1"), anyList())).thenReturn(true);

        assertThrows(SeatAlreadyBookedException.class, () -> {
            bookingService.initiateBooking(userId, showId, screenId, seatIds, totalAmount);
        });
    }

    //  Test for confirmBooking()
    @Test
    void testConfirmBooking_Success() {
        String bookingId = UUID.randomUUID().toString();
        String paymentId = "payment123";

        Booking booking = new Booking(bookingId, "user123", "show1", "screen1", LocalDateTime.now(),
                BookingStatus.PENDING, null, 400.0, null);

        BookedSeat seat1 = new BookedSeat("A1", "screen1", BookingStatus.PENDING, LocalDateTime.now(), booking);
        booking.setSeats(List.of(seat1));

        when(bookingRepo.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any())).thenReturn(booking);

        Booking confirmed = bookingService.confirmBooking(bookingId, paymentId);

        assertEquals(BookingStatus.CONFIRMED, confirmed.getStatus());
        assertEquals(paymentId, confirmed.getPaymentId());
        assertEquals(BookingStatus.CONFIRMED, confirmed.getSeats().get(0).getStatus());

        verify(eventPublisher, times(1)).publishEvent(any());
        verify(bookingRepo, times(1)).save(booking);
    }

    @Test
    void testConfirmBooking_NotFound_ThrowsException() {
        String bookingId = "non-existing";
        when(bookingRepo.findById(bookingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                bookingService.confirmBooking(bookingId, "pay123")
        );

        assertTrue(exception.getMessage().contains("Booking not found"));
    }
}
