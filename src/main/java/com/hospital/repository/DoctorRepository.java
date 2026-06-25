package com.hospital.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.entity.Doctor;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByPhoneNumber(String phone);
}
