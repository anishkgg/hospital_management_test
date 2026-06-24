package com.hospital.Controller;

import com.hospital.Service.DoctorService;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
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

    @PostMapping("/addDoctors")
    public ResponseEntity<DoctorResponseDTO> addDoctor (@RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO createDoctor = doctorService.createDoctor(doctorRequestDTO);
        return new ResponseEntity<>(createDoctor, HttpStatus.CREATED);
    }
}
