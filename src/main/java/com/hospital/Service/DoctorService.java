package com.hospital.Service;

import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO);
    List<DoctorResponseDTO> getAllDoctors();
    List<DoctorResponseDTO> searchDoctors(String name, String specialty, String city);
}
