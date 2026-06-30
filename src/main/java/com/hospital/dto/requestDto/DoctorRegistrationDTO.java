package com.hospital.dto.requestDto;

import lombok.Builder;
import java.time.LocalTime;

@Builder
public record DoctorRegistrationDTO(
        String name,
        String specialty,
        String phone,
        String email,
        Long hospitalId,
        String licenseNumber,
        LocalTime shiftStart,
        LocalTime shiftEnd,
        String workingDays,
        Double rating
) {}
