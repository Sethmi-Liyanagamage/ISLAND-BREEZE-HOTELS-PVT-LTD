package com.hotelmanagementsystem.events;

import com.hotelmanagementsystem.model.Booking;
import org.springframework.context.ApplicationEvent;

public class BookingCreatedEvent extends ApplicationEvent {
    private final Booking booking;

    public BookingCreatedEvent(Object source, Booking booking) {
        super(source);
        this.booking = booking;
    }

    public Booking getBooking() {
        return booking;
    }
}



