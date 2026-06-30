package com.hospital.Service.imp;

import com.hospital.Service.HospitalService;
import com.hospital.Utils.ValidationUtils;
import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;
import com.hospital.entity.Hospital;
import com.hospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public HospitalResponseDTO createHospital(HospitalRequestDTO hospitalRequestDTO) {
        if (!ValidationUtils.isValidIndianPhoneNumber(hospitalRequestDTO.phone())) {
            throw new IllegalArgumentException("Invalid Indian Phone Number");
        }

        if (hospitalRepository.existsByNameAndCity(hospitalRequestDTO.hospitalName(), hospitalRequestDTO.city())) {
            throw new IllegalArgumentException("Hospital is Already Exist");
        }

        Hospital hospital = Hospital.builder()
                .name(hospitalRequestDTO.hospitalName())
                .address(hospitalRequestDTO.address())
                .phone(hospitalRequestDTO.phone())
                .city(hospitalRequestDTO.city())
                .rating(hospitalRequestDTO.rating() != null ? hospitalRequestDTO.rating() : 5.0)
                .build();

        Hospital saveHospital = hospitalRepository.save(hospital);

        return HospitalResponseDTO.builder()
                .id(saveHospital.getId())
                .name(saveHospital.getName())
                .address(saveHospital.getAddress())
                .phone(saveHospital.getPhone())
                .city(saveHospital.getCity())
                .rating(saveHospital.getRating())
                .build();
    }

    @Override
    public List<HospitalResponseDTO> getAllHospitals() {
        List<Hospital> hospitals = hospitalRepository.findAll();

        return hospitals.stream()
                .map(hospital -> HospitalResponseDTO.builder()
                        .id(hospital.getId())
                        .name(hospital.getName())
                        .address(hospital.getAddress())
                        .phone(hospital.getPhone())
                        .city(hospital.getCity())
                        .rating(hospital.getRating() != null ? hospital.getRating() : 5.0)
                        .build())
                .collect(Collectors.toList());
    }
}
