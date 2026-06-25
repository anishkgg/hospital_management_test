package com.hospital.dto.responseDto;

import lombok.Builder;

@Builder
public record HospitalResponseDTO(
        Long id,
        String name,
        String address,
        String phone,
        String city
) {}
