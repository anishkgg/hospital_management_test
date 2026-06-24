package com.hospital.dto.responseDto;

import lombok.Builder;

import java.util.List;

@Builder
public record HospitalResponseDTO(
        Long id,
        String name,
        String address,
        String phone,
        List<Long> doctorIds
) {}
