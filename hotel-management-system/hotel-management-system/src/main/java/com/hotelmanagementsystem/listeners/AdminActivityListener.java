package com.hotelmanagementsystem.listeners;

import com.hotelmanagementsystem.events.BookingCreatedEvent;
import com.hotelmanagementsystem.events.PaymentSucceededEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
public class AdminActivityListener {

    private static final String LOG_PATH = "src/main/resources/data/admin_activity.txt";

    @Async
    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        String line = LocalDateTime.now() + ",Booking created: " + event.getBooking().getBookingId() + ",BOOKING_CREATED\n";
        append(line);
    }

    @Async
    @EventListener
    public void onPaymentSucceeded(PaymentSucceededEvent event) {
        String line = LocalDateTime.now() + ",Payment succeeded: " + event.getPayment().getPaymentId() + ",PAYMENT\n";
        append(line);
    }

    private void append(String line) {
        try {
            Path path = Paths.get(LOG_PATH);
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile(), true)) {
                writer.write(line);
            }
        } catch (IOException e) {
            // swallow to avoid impacting main flow; in prod use proper logging
        }
    }
}


