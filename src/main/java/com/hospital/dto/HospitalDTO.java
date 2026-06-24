package com.hospital.dto;

import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;
import java.util.List;

public record HospitalDTO(
    Long id,
    String name,
    String address,
    String phone,
    List<Long> doctorIds
) {
    public static HospitalDTO fromEntity(Hospital hospital) {
        if (hospital == null) return null;
        List<Long> ids = hospital.getDoctors() != null ? 
            hospital.getDoctors().stream().map(Doctor::getId).toList() : List.of();
        return new HospitalDTO(
            hospital.getId(),
            hospital.getName(),
            hospital.getAddress(),
            hospital.getPhone(),
            ids
        );
    }

    public Hospital toEntity() {
        return Hospital.builder()
            .id(this.id)
            .name(this.name)
            .address(this.address)
            .phone(this.phone)
            .build();
    }
}
