package com.hospital.Service.imp;

import com.hospital.Service.DoctorService;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        if (doctorRepository.existsByLicenseNumber(doctorRequestDTO.licenseNumber())) {
            throw new IllegalArgumentException("Doctor is already exist");
        }

        if (doctorRepository.existsByPhone(doctorRequestDTO.phone())) {
            throw new IllegalArgumentException("Phone Number Can not Same");
        }

        Hospital hospital = hospitalRepository.findById(doctorRequestDTO.hospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        Doctor doctor = Doctor.builder()
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

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(doctor -> DoctorResponseDTO.builder()
                        .id(doctor.getId())
                        .name(doctor.getName())
                        .specialty(doctor.getSpecialty())
                        .phone(doctor.getPhone())
                        .hospitalId(doctor.getHospital() != null ? doctor.getHospital().getId() : null)
                        .hospitalName(doctor.getHospital() != null ? doctor.getHospital().getName() : "N/A")
                        .build())
                .collect(Collectors.toList());
    }
}
