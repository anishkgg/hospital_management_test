package com.hospital.dto.responseDto;

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
        String status
) {}
