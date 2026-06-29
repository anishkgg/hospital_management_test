package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByPatientPhone(String patientPhone);
    boolean existsByBookingCode(String bookingCode);
    Optional<Appointment> findByBookingCode(String bookingCode);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.status <> com.hospital.Enum.AppointmentStatus.CANCELLED " +
           "AND a.appointmentTime > :startTime AND a.appointmentTime < :endTime")
    boolean existsOverlappingAppointment(@Param("doctorId") Long doctorId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);
}

