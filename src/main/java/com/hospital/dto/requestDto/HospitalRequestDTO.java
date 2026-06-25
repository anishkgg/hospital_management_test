package com.hospital.dto.requestDto;

import lombok.Builder;

@Builder
public record HospitalRequestDTO(
    String hospitalName,
    String address,
    String phone,
    String city
) {}
