package com.hospital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "appointment_table")
@ToString
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String patientName;
    private String patientPhone;
    private LocalDateTime appointmentTime;
    @ManyToOne
    private Doctor doctor;
    private String status; // e.g., SCHEDULED, CANCELLED, COMPLETED

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        if (doctor != null && !doctor.getAppointments().contains(this)) {
            doctor.addAppointment(this);
        }
    }
}
