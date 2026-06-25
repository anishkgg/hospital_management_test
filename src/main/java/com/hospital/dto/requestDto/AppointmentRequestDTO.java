package com.hospital.dto.requestDto;

import com.hospital.Enum.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestDTO(
    Long id,
    String patientName,
    String patientPhone,
    LocalDateTime appointmentTime,
    Long doctorId,
    AppointmentStatus status
) {}
