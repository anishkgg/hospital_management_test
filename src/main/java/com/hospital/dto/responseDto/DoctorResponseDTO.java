package com.hospital.dto.responseDto;

import lombok.Builder;

@Builder
public record DoctorResponseDTO(
        Long id,
        String name,
        String specialty,
        String phone,
        String email,
        Long hospitalId,
        String hospitalName,
        String licenseNumber,
        Double rating
) {}
