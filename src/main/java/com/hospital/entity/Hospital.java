package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
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

    private Double rating;

    @OneToMany(mappedBy = "hospital")
    private List<Doctor> doctors = new ArrayList<>();
}
