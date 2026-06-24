package com.hospital.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"hospital", "appointments"})
public class Doctor {
    private Long id;
    private String name;
    private String specialty;
    private String phone;
    private Hospital hospital;
    @Builder.Default
    private List<Appointment> appointments = new ArrayList<>();

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
        if (hospital != null && !hospital.getDoctors().contains(this)) {
            hospital.addDoctor(this);
        }
    }

    public void addAppointment(Appointment appointment) {
        if (appointment != null) {
            this.appointments.add(appointment);
            if (appointment.getDoctor() != this) {
                appointment.setDoctor(this);
            }
        }
    }
}
