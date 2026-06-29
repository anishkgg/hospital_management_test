package com.hospital.dto.requestDto;

import java.time.LocalDateTime;

public record AppointmentRescheduleRequestDTO(
        LocalDateTime newTime
) {}
