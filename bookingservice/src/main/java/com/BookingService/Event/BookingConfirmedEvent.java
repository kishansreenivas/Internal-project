package com.BookingService.Event;

import org.springframework.context.ApplicationEvent;


public class BookingConfirmedEvent extends ApplicationEvent {
    private final String bookingId;
    public BookingConfirmedEvent(Object source, String bookingId) {
        super(source);
        this.bookingId = bookingId;
    }
    public String getBookingId() { return bookingId; }
}