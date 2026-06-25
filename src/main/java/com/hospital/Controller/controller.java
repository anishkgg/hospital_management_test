package com.hospital.Controller;

import com.hospital.Service.DoctorService;
import com.hospital.Service.HospitalService;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class controller {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/addDoctors")
    public ResponseEntity<DoctorResponseDTO> addDoctor (@RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO createDoctor = doctorService.createDoctor(doctorRequestDTO);
        return new ResponseEntity<>(createDoctor, HttpStatus.CREATED);
    }

    @PostMapping("/addHospital")
    public ResponseEntity<HospitalResponseDTO> addHospital (@RequestBody HospitalRequestDTO hospitalRequestDTO) {
        HospitalResponseDTO createHospital = hospitalService.createHospital(hospitalRequestDTO);
        return new ResponseEntity<>(createHospital, HttpStatus.CREATED);
    }
}
