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
@ToString(exclude = "doctors")
public class Hospital {
    private Long id;
    private String name;
    private String address;
    private String phone;
    @Builder.Default
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
