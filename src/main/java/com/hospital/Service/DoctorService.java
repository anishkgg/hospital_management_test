package com.hospital.Service;

import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.requestDto.DoctorRegistrationDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.dto.responseDto.DoctorDetailsResponseDTO;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO);
    DoctorDetailsResponseDTO createDoctorWithSchedule(DoctorRegistrationDTO doctorRegistrationDTO);
    List<DoctorResponseDTO> getAllDoctors();
    List<DoctorDetailsResponseDTO> getAllDoctorsWithSchedule();
    List<DoctorDetailsResponseDTO> searchDoctorsWithSchedule(String name, String specialty, String city);
}
