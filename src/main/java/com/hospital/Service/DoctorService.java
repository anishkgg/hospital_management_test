package com.hospital.Service;

import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        if (doctorRepository.existsByLicenseNumber(doctorRequestDTO.licenseNumber())) {
            throw new IllegalArgumentException("Doctor is already exist");
        }

        Hospital hospital = hospitalRepository.findById(doctorRequestDTO.hospitalId()).orElseThrow();

        Doctor doctor = Doctor.builder()
                .id(doctorRequestDTO.id())
                .name(doctorRequestDTO.name())
                .specialty(doctorRequestDTO.specialty())
                .phone(doctorRequestDTO.phone())
                .hospital(hospital)
                .licenseNumber(doctorRequestDTO.licenseNumber())
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return DoctorResponseDTO.builder()
                .id(savedDoctor.getId())
                .name(savedDoctor.getName())
                .specialty(savedDoctor.getSpecialty())
                .phone(savedDoctor.getPhone())
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .licenseNumber(savedDoctor.getLicenseNumber())
                .build();
    }
}
