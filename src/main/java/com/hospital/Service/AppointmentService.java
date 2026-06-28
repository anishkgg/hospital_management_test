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

        if (appointmentRepository.existsByPatientPhone(appointmentRequestDTO.patientPhone())) {
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

        return convertToResponseDTO(saveAppointment);
    }

    public List<AppointmentResponseDTO> getAllAppointment() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // ************* Cancelled Appointment **********************

    public AppointmentResponseDTO cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not Found"));

        if(appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Complete Appointment can not be Cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment cancelledAppointment = appointmentRepository.save(appointment);
        return convertToResponseDTO(cancelledAppointment);
    }

    // **************** Complete Appointment *************************

    public AppointmentResponseDTO completeAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment is Completed"));

        if(appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalArgumentException("Already Cancelled Appointment");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        Appointment completeAppointment = appointmentRepository.save(appointment);
        return convertToResponseDTO(completeAppointment);
    }

    private AppointmentResponseDTO convertToResponseDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .patientName(appointment.getPatientName())
                .patientPhone(appointment.getPatientPhone())
                .appointmentTime(appointment.getAppointmentTime())
                .doctorId(appointment.getDoctor() != null ? appointment.getDoctor().getId() : null)
                .doctorName(appointment.getDoctor() != null ? appointment.getDoctor().getName() : "N/A")
                .status(appointment.getStatus())
                .build();
    }
}
