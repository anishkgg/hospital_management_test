package com.hospital.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
    boolean existsByPhone(String phone);

    @Query("SELECT d FROM Doctor d JOIN d.hospital h WHERE " +
           "(:name IS NULL OR :name = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:specialty IS NULL OR :specialty = '' OR LOWER(d.specialty) = LOWER(:specialty)) AND " +
           "(:city IS NULL OR :city = '' OR LOWER(h.city) = LOWER(:city))")
    List<Doctor> searchDoctors(@Param("name") String name,
                               @Param("specialty") String specialty,
                               @Param("city") String city);
}
