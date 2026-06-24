package com.hospital.dto;

import com.hospital.entity.Appointment;
import com.hospital.entity.Doctor;
import java.time.LocalDateTime;

public record AppointmentDTO(
    Long id,
    String patientName,
    String patientPhone,
    LocalDateTime appointmentTime,
    Long doctorId,
    String doctorName,
    String status
) {
    public static AppointmentDTO fromEntity(Appointment appointment) {
        if (appointment == null) return null;
        return new AppointmentDTO(
            appointment.getId(),
            appointment.getPatientName(),
            appointment.getPatientPhone(),
            appointment.getAppointmentTime(),
            appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
            appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
            appointment.getStatus()
        );
    }

    public Appointment toEntity(Doctor doctor) {
        return Appointment.builder()
            .id(this.id)
            .patientName(this.patientName)
            .patientPhone(this.patientPhone)
            .appointmentTime(this.appointmentTime)
            .doctor(doctor)
            .status(this.status)
            .build();
    }
}
