package com.hospital.dto.responseDto;

import lombok.Builder;
import java.time.LocalTime;

@Builder
public record DoctorDetailsResponseDTO(
        Long id,
        String name,
        String specialty,
        String phone,
        String email,
        Long hospitalId,
        String hospitalName,
        String licenseNumber,
        LocalTime shiftStart,
        LocalTime shiftEnd,
        String workingDays
) {}
