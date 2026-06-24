package com.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "doctor_table")
@ToString(exclude = {"hospital", "appointments"})
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String specialty;
    private String phone;
    @ManyToOne
    private Hospital hospital;
    @Column(unique = true)
    private String licenseNumber;
    @Builder.Default
    @OneToMany(mappedBy = "doctor")
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
