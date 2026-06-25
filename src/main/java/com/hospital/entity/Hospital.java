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
    private Long hospitalId;
    @Column(name = "hospital_name", nullable = false)
    private String hospitalName;
    private String address;
    private String phone;
    @Column(name = "city_name", nullable = false)
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
