package com.hospital.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

    @Async
    @EventListener
    public void handleAppointmentBooked(AppointmentBookedEvent event) {
        log.info("--------------------------------------------------------------------------------");
        log.info("SMS/Email Notification Async Triggered");
        log.info("Recipient Phone: {}", event.patientPhone());
        log.info("Message: Hello {}, your appointment with Dr. {} on {} has been confirmed. Booking Code: {}",
                event.patientName(), event.doctorName(), event.appointmentTime(), event.bookingCode());
        log.info("--------------------------------------------------------------------------------");
    }

    @Async
    @EventListener
    public void handleAppointmentCancelled(AppointmentCancelledEvent event) {
        log.info("--------------------------------------------------------------------------------");
        log.info("SMS/Email Notification Async Triggered");
        log.info("Recipient Phone: {}", event.patientPhone());
        log.info("Message: Hello {}, your appointment (Code: {}) has been successfully CANCELLED.",
                event.patientName(), event.bookingCode());
        log.info("--------------------------------------------------------------------------------");
    }
}
