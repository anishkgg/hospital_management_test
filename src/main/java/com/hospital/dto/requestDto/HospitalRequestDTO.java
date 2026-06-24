package com.hospital.dto.requestDto;

import lombok.Builder;

@Builder
public record HospitalRequestDTO(
    Long id,
    String name,
    String address,
    String phone
) {}
