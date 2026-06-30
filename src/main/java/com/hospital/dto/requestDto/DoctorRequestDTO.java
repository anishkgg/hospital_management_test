package com.hospital.dto.requestDto;

import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public record DoctorRequestDTO(String name,
                               String specialty,
                               String phone,
                               String email,
                               Long hospitalId,
                               String licenseNumber,
                               Double rating) {}
