package com.hospital.Event;

import java.time.LocalDateTime;

public record AppointmentBookedEvent(
        Long appointmentId,
        String patientName,
        String patientPhone,
        String doctorName,
        LocalDateTime appointmentTime,
        String bookingCode
) {}
