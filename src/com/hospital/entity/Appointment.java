package com.hospital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Appointment {
    private Long id;
    private String patientName;
    private String patientPhone;
    private LocalDateTime appointmentTime;
    private Doctor doctor;
    private String status; // e.g., SCHEDULED, CANCELLED, COMPLETED

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        if (doctor != null && !doctor.getAppointments().contains(this)) {
            doctor.addAppointment(this);
        }
    }
}
