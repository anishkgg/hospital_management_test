package com.hospital.dto.requestDto;

import java.time.LocalDateTime;

public record AppointmentRequestDTO(
        String patientName,
        String patientPhone,
        String patientEmail,
        LocalDateTime appointmentTime,
        Long doctorId
) {}
