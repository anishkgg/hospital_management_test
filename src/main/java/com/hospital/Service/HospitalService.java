package com.hospital.Service;

import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;
import com.hospital.entity.Hospital;
import com.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public HospitalResponseDTO createHospital(HospitalRequestDTO hospitalRequestDTO) {

        if (hospitalRepository.existsByNameAndCity(hospitalRequestDTO.hospitalName(), hospitalRequestDTO.city())) {
            throw new IllegalArgumentException("Hospital is Already Exist");
        }

        Hospital hospital = Hospital.builder()
                .hospitalId(hospitalRequestDTO.hospitalId())
                .hospitalName(hospitalRequestDTO.hospitalName())
                .address(hospitalRequestDTO.address())
                .phone(hospitalRequestDTO.phone())
                .city(hospitalRequestDTO.city())
                .build();

        Hospital saveHospital = hospitalRepository.save(hospital);

        return HospitalResponseDTO.builder()
                .id(saveHospital.getHospitalId())
                .name(saveHospital.getHospitalName())
                .address(saveHospital.getAddress())
                .phone(saveHospital.getPhone())
                .build();

    }
}
