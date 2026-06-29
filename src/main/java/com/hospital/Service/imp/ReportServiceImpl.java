package com.hospital.Service.imp;

import com.hospital.Enum.AppointmentStatus;
import com.hospital.Service.ReportService;
import com.hospital.dto.responseDto.HospitalReportDTO;
import com.hospital.repository.AppointmentReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AppointmentReportRepository appointmentReportRepository;

    @Override
    public HospitalReportDTO getAnalyticsSummary() {
        long totalAppointments = appointmentReportRepository.count();
        long totalScheduled = appointmentReportRepository.countByStatus(AppointmentStatus.SCHEDULED);
        long totalCompleted = appointmentReportRepository.countByStatus(AppointmentStatus.COMPLETED);
        long totalCancelled = appointmentReportRepository.countByStatus(AppointmentStatus.CANCELLED);

        double cancellationRate = 0.0;
        if (totalAppointments > 0) {
            cancellationRate = ((double) totalCancelled / totalAppointments) * 100.0;
        }

        List<String> activeDoctors = appointmentReportRepository.findMostActiveDoctor(PageRequest.of(0, 1));
        String mostActiveDoctor = activeDoctors.isEmpty() ? "N/A" : activeDoctors.get(0);

        List<String> busyHospitals = appointmentReportRepository.findBusiestHospital(PageRequest.of(0, 1));
        String busiestHospital = busyHospitals.isEmpty() ? "N/A" : busyHospitals.get(0);

        return HospitalReportDTO.builder()
                .totalAppointments(totalAppointments)
                .totalScheduled(totalScheduled)
                .totalCompleted(totalCompleted)
                .totalCancelled(totalCancelled)
                .cancellationRate(cancellationRate)
                .mostActiveDoctor(mostActiveDoctor)
                .busiestHospital(busiestHospital)
                .build();
    }
}
