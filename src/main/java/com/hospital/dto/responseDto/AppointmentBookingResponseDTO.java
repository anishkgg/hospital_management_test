package com.hospital.dto.responseDto;

import com.hospital.Enum.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentBookingResponseDTO(
    String bookingCode,
    AppointmentStatus status,
    LocalDateTime appointmentTime
){}
