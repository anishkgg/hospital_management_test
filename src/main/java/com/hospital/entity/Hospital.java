package com.hospital.entity;

import jakarta.persistence.*;
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
@Table(name = "hospital_table")
@ToString(exclude = "doctors")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String city;
    @Builder.Default
    @OneToMany(mappedBy = "hospital")
    private List<Doctor> doctors = new ArrayList<>();

    public void addDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctors.add(doctor);
            if (doctor.getHospital() != this) {
                doctor.setHospital(this);
            }
        }
    }
}
