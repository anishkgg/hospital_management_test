package com.hospital.repository;

import com.hospital.entity.Appointment;
import com.hospital.Enum.AppointmentStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentReportRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.status <> com.hospital.Enum.AppointmentStatus.CANCELLED " +
           "AND a.status <> com.hospital.Enum.AppointmentStatus.WAITING " +
           "AND a.appointmentTime > :startTime AND a.appointmentTime < :endTime")
    boolean existsOverlappingAppointment(@Param("doctorId") Long doctorId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.id <> :excludeAppointmentId " +
           "AND a.status <> com.hospital.Enum.AppointmentStatus.CANCELLED " +
           "AND a.status <> com.hospital.Enum.AppointmentStatus.WAITING " +
           "AND a.appointmentTime > :startTime AND a.appointmentTime < :endTime")
    boolean existsOverlappingAppointmentForReschedule(@Param("doctorId") Long doctorId,
                                                      @Param("excludeAppointmentId") Long excludeAppointmentId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    long countByStatus(AppointmentStatus status);

    @Query("SELECT a.doctor.name FROM Appointment a GROUP BY a.doctor.name ORDER BY COUNT(a) DESC")
    List<String> findMostActiveDoctor(Pageable pageable);

    @Query("SELECT a.doctor.hospital.name FROM Appointment a GROUP BY a.doctor.hospital.name ORDER BY COUNT(a) DESC")
    List<String> findBusiestHospital(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = com.hospital.Enum.AppointmentStatus.WAITING " +
           "AND a.appointmentTime > :startTime AND a.appointmentTime < :endTime " +
           "ORDER BY a.id ASC")
    List<Appointment> findFirstWaitingAppointmentForSlot(@Param("doctorId") Long doctorId,
                                                         @Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime,
                                                         Pageable pageable);
}
