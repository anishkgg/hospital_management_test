package com.hospital.dto.responseDto;

import com.hospital.Enum.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentResponseDTO(
        Long id,
        String patientName,
        String patientPhone,
        LocalDateTime appointmentTime,
        Long doctorId,
        String doctorName,
        AppointmentStatus status,
        String bookingCode,
        String medicalNotes
) {}
