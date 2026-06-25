package com.hospital.Controller;

import com.hospital.Service.AppointmentService;
import com.hospital.Service.DoctorService;
import com.hospital.Service.HospitalService;
import com.hospital.dto.requestDto.AppointmentRequestDTO;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.AppointmentResponseDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class controller {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private AppointmentService appointmentService;

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

    @GetMapping("/getAllDoctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/getAllHospitals")
    public ResponseEntity<List<HospitalResponseDTO>> getAllHospital() {
        List<HospitalResponseDTO> hospitals= hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    @PostMapping("book/appointment")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment (@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentResponseDTO appointments = appointmentService.bookAppointment(appointmentRequestDTO);
        return new ResponseEntity<>(appointments, HttpStatus.CREATED);
    }

    @GetMapping("getAllAppointment")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointment () {
        List<AppointmentResponseDTO> appointmentResponseDTO = appointmentService.getAllAppointment();
        return ResponseEntity.ok(appointmentResponseDTO);
    }

}
