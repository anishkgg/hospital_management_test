package com.hospital.Controller;

import com.hospital.Service.AppointmentService;
import com.hospital.Service.DoctorService;
import com.hospital.Service.HospitalService;
import com.hospital.Service.ReportService;
import com.hospital.dto.requestDto.AppointmentCompleteRequestDTO;
import com.hospital.dto.requestDto.AppointmentRequestDTO;
import com.hospital.dto.requestDto.AppointmentRescheduleRequestDTO;
import com.hospital.dto.requestDto.DoctorRequestDTO;
import com.hospital.dto.requestDto.DoctorRegistrationDTO;
import com.hospital.dto.requestDto.HospitalRequestDTO;
import com.hospital.dto.responseDto.AppointmentBookingResponseDTO;
import com.hospital.dto.responseDto.AppointmentResponseDTO;
import com.hospital.dto.responseDto.DoctorResponseDTO;
import com.hospital.dto.responseDto.DoctorDetailsResponseDTO;
import com.hospital.dto.responseDto.HospitalResponseDTO;
import com.hospital.dto.responseDto.HospitalReportDTO;
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

    @Autowired
    private ReportService reportService;

    @PostMapping("/addHospital")
    public ResponseEntity<HospitalResponseDTO> addHospital (@RequestBody HospitalRequestDTO hospitalRequestDTO) {
        HospitalResponseDTO createHospital = hospitalService.createHospital(hospitalRequestDTO);
        return new ResponseEntity<>(createHospital, HttpStatus.CREATED);
    }

    @PostMapping("/addDoctors")
    public ResponseEntity<DoctorResponseDTO> addDoctor (@RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO createDoctor = doctorService.createDoctor(doctorRequestDTO);
        return new ResponseEntity<>(createDoctor, HttpStatus.CREATED);
    }

    @PostMapping("/addDoctors/schedule")
    public ResponseEntity<DoctorDetailsResponseDTO> addDoctorWithSchedule (@RequestBody DoctorRegistrationDTO doctorRegistrationDTO) {
        DoctorDetailsResponseDTO createDoctor = doctorService.createDoctorWithSchedule(doctorRegistrationDTO);
        return new ResponseEntity<>(createDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/getAllDoctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/getAllDoctors/schedule")
    public ResponseEntity<List<DoctorDetailsResponseDTO>> getAllDoctorsWithSchedule() {
        List<DoctorDetailsResponseDTO> doctors = doctorService.getAllDoctorsWithSchedule();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/search")
    public ResponseEntity<List<DoctorDetailsResponseDTO>> searchDoctors(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "specialty", required = false) String specialty,
            @RequestParam(value = "city", required = false) String city) {
        List<DoctorDetailsResponseDTO> doctors = doctorService.searchDoctorsWithSchedule(name, specialty, city);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/getAllHospitals")
    public ResponseEntity<List<HospitalResponseDTO>> getAllHospital() {
        List<HospitalResponseDTO> hospitals= hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    @PostMapping("book/appointment")
    public ResponseEntity<AppointmentBookingResponseDTO> bookAppointment (@RequestBody AppointmentRequestDTO appointmentRequestDTO) {
        AppointmentBookingResponseDTO appointments = appointmentService.bookAppointment(appointmentRequestDTO);
        return new ResponseEntity<>(appointments, HttpStatus.CREATED);
    }

    @GetMapping("getAllAppointment")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointment() {
        List<AppointmentResponseDTO> appointmentResponseDTO = appointmentService.getAllAppointment();
        return ResponseEntity.ok(appointmentResponseDTO);
    }

    @GetMapping("/appointment/details")
    public ResponseEntity<AppointmentResponseDTO> getBookingDetails(@RequestParam("bookingCode") String bookingCode) {
        AppointmentResponseDTO getAppointmentDetails = appointmentService.getAppointmentByBookingCode(bookingCode);
        return ResponseEntity.ok(getAppointmentDetails);
    }

    @PutMapping("appointment/cancel")
    public ResponseEntity<AppointmentResponseDTO> AppointmentCancellation(@RequestParam("id") Long appointmentId) {
        AppointmentResponseDTO cancelAppointment = appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok(cancelAppointment);
    }

    @PutMapping("appointment/complete")
    public ResponseEntity<AppointmentResponseDTO> AppointmentComplete(
            @RequestParam("id") Long appointmentId,
            @RequestBody(required = false) AppointmentCompleteRequestDTO requestDTO) {
        AppointmentResponseDTO completeAppointment = appointmentService.completeAppointment(appointmentId, requestDTO);
        return ResponseEntity.ok(completeAppointment);
    }

    @PutMapping("appointment/reschedule")
    public ResponseEntity<AppointmentResponseDTO> AppointmentReschedule(
            @RequestParam("id") Long appointmentId,
            @RequestBody AppointmentRescheduleRequestDTO requestDTO) {
        AppointmentResponseDTO rescheduleAppointment = appointmentService.rescheduleAppointment(appointmentId, requestDTO);
        return ResponseEntity.ok(rescheduleAppointment);
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<HospitalReportDTO> getReportSummary() {
        HospitalReportDTO summary = reportService.getAnalyticsSummary();
        return ResponseEntity.ok(summary);
    }
}
