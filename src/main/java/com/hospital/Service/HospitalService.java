package com.hospital.Service;

import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;

import java.util.List;

public interface HospitalService {
    HospitalResponseDTO createHospital(HospitalRequestDTO hospitalRequestDTO);
    List<HospitalResponseDTO> getAllHospitals();
}
