package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPatientPhone(String patientPhone);
    boolean existsByBookingCode(String bookingCode);
    Optional<Appointment> findByBookingCode(String bookingCode);
}
