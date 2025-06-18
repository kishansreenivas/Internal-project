package com.BookingService.Event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookingEventListener {
    @EventListener
    public void onBookingConfirmed(BookingConfirmedEvent evt) {
        // send notifications
    }
}