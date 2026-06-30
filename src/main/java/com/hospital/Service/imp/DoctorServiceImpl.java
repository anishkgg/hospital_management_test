package com.hospital.Service.imp;

import com.hospital.Service.DoctorService;
import com.hospital.Utils.ValidationUtils;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.requestDto.DoctorRegistrationDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.dto.responseDto.DoctorDetailsResponseDTO;
import com.hospital.entity.Doctor;
import com.hospital.entity.Hospital;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
        if (!ValidationUtils.isValidIndianPhoneNumber(doctorRequestDTO.phone())) {
            throw new IllegalArgumentException("Invalid Indian Phone Number");
        }

        if (doctorRequestDTO.email() != null && !ValidationUtils.isValidEmail(doctorRequestDTO.email())) {
            throw new IllegalArgumentException("Invalid Email Address");
        }

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
                .email(doctorRequestDTO.email())
                .hospital(hospital)
                .licenseNumber(doctorRequestDTO.licenseNumber())
                .shiftStart(LocalTime.of(9, 0))
                .shiftEnd(LocalTime.of(17, 0))
                .workingDays("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY")
                .rating(doctorRequestDTO.rating() != null ? doctorRequestDTO.rating() : 5.0)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return DoctorResponseDTO.builder()
                .id(savedDoctor.getId())
                .name(savedDoctor.getName())
                .specialty(savedDoctor.getSpecialty())
                .phone(savedDoctor.getPhone())
                .email(savedDoctor.getEmail())
                .hospitalId(hospital.getId())
                .hospitalName(hospital.getName())
                .licenseNumber(savedDoctor.getLicenseNumber())
                .rating(savedDoctor.getRating())
                .build();
    }

    @Override
    public DoctorDetailsResponseDTO createDoctorWithSchedule(DoctorRegistrationDTO dto) {
        if (!ValidationUtils.isValidIndianPhoneNumber(dto.phone())) {
            throw new IllegalArgumentException("Invalid Indian Phone Number");
        }

        if (dto.email() != null && !ValidationUtils.isValidEmail(dto.email())) {
            throw new IllegalArgumentException("Invalid Email Address");
        }

        if (doctorRepository.existsByLicenseNumber(dto.licenseNumber())) {
            throw new IllegalArgumentException("Doctor is already exist");
        }

        if (doctorRepository.existsByPhone(dto.phone())) {
            throw new IllegalArgumentException("Phone Number Can not Same");
        }

        Hospital hospital = hospitalRepository.findById(dto.hospitalId())
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        LocalTime start = dto.shiftStart() != null ? dto.shiftStart() : LocalTime.of(9, 0);
        LocalTime end = dto.shiftEnd() != null ? dto.shiftEnd() : LocalTime.of(17, 0);
        String days = dto.workingDays() != null && !dto.workingDays().isBlank()
                ? dto.workingDays().toUpperCase()
                : "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY";

        Doctor doctor = Doctor.builder()
                .name(dto.name())
                .specialty(dto.specialty())
                .phone(dto.phone())
                .email(dto.email())
                .hospital(hospital)
                .licenseNumber(dto.licenseNumber())
                .shiftStart(start)
                .shiftEnd(end)
                .workingDays(days)
                .rating(dto.rating() != null ? dto.rating() : 5.0)
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return convertToDetailsResponseDTO(savedDoctor);
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
                        .email(doctor.getEmail())
                        .hospitalId(doctor.getHospital() != null ? doctor.getHospital().getId() : null)
                        .hospitalName(doctor.getHospital() != null ? doctor.getHospital().getName() : "N/A")
                        .licenseNumber(doctor.getLicenseNumber())
                        .rating(doctor.getRating() != null ? doctor.getRating() : 5.0)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDetailsResponseDTO> getAllDoctorsWithSchedule() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .map(this::convertToDetailsResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorDetailsResponseDTO> searchDoctorsWithSchedule(String name, String specialty, String city) {
        List<Doctor> doctors = doctorRepository.searchDoctors(name, specialty, city);

        return doctors.stream()
                .map(this::convertToDetailsResponseDTO)
                .collect(Collectors.toList());
    }

    private DoctorDetailsResponseDTO convertToDetailsResponseDTO(Doctor doctor) {
        return DoctorDetailsResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialty(doctor.getSpecialty())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .hospitalId(doctor.getHospital() != null ? doctor.getHospital().getId() : null)
                .hospitalName(doctor.getHospital() != null ? doctor.getHospital().getName() : "N/A")
                .licenseNumber(doctor.getLicenseNumber())
                .shiftStart(doctor.getShiftStart() != null ? doctor.getShiftStart() : LocalTime.of(9, 0))
                .shiftEnd(doctor.getShiftEnd() != null ? doctor.getShiftEnd() : LocalTime.of(17, 0))
                .workingDays(doctor.getWorkingDays() != null ? doctor.getWorkingDays() : "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY")
                .rating(doctor.getRating() != null ? doctor.getRating() : 5.0)
                .build();
    }
}
