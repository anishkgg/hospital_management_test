package com.hospital.dto;

import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;

public record DoctorDTO(
    Long id,
    String name,
    String specialty,
    String phone,
    Long hospitalId,
    String hospitalName
) {
    public static DoctorDTO fromEntity(Doctor doctor) {
        if (doctor == null) return null;
        return new DoctorDTO(
            doctor.getId(),
            doctor.getName(),
            doctor.getSpecialty(),
            doctor.getPhone(),
            doctor.getHospital() != null ? doctor.getHospital().getId() : null,
            doctor.getHospital() != null ? doctor.getHospital().getName() : null
        );
    }

    public Doctor toEntity(Hospital hospital) {
        return Doctor.builder()
            .id(this.id)
            .name(this.name)
            .specialty(this.specialty)
            .phone(this.phone)
            .hospital(hospital)
            .build();
    }
}
