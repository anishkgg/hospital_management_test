package com.hospital.Service;

import com.hospital.Enum.AppointmentStatus;
import com.hospital.dto.requestDto.AppointmentRequestDTO;
import com.hospital.dto.responseDto.AppointmentResponseDTO;
import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO appointmentRequestDTO) {
        if (appointmentRequestDTO.appointmentTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Appointment in Future");
        }

        if(appointmentRepository.existsByPatientPhone(appointmentRequestDTO.patientPhone())) {
            throw new IllegalArgumentException("Phone Number Can not Same");
        }

        Doctor doctor = doctorRepository.findById(appointmentRequestDTO.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor is not Found"));

        Appointment appointment = Appointment.builder()
                .patientName(appointmentRequestDTO.patientName())
                .patientPhone(appointmentRequestDTO.patientPhone())
                .appointmentTime(appointmentRequestDTO.appointmentTime())
                .doctor(doctor)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saveAppointment = appointmentRepository.save(appointment);

        return AppointmentResponseDTO.builder()
                .id(saveAppointment.getId())
                .patientName(saveAppointment.getPatientName())
                .patientPhone(saveAppointment.getPatientPhone())
                .appointmentTime(saveAppointment.getAppointmentTime())
                .doctorId(doctor.getId())
                .doctorName(doctor.getName())
                .status(saveAppointment.getStatus())
                .build();
    }

    public List<AppointmentResponseDTO> getAllAppointment() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .map(appointment -> AppointmentResponseDTO.builder()
                        .id(appointment.getId())
                        .patientName(appointment.getPatientName())
                        .patientPhone(appointment.getPatientPhone())
                        .appointmentTime(appointment.getAppointmentTime())
                        .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                        .doctorName(appointment.getDoctor() != null ? appointment.getDoctor().getName() : null)
                        .status(appointment.getStatus())
                        .build())
                .collect(Collectors.toList());
    }
}
