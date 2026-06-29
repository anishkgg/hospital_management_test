package com.hospital.Event;

public record AppointmentCancelledEvent(
        Long appointmentId,
        String patientName,
        String patientPhone,
        String bookingCode
) {}
