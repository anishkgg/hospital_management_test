package com.hospital.dto.responseDto;

import lombok.Builder;

@Builder
public record HospitalReportDTO(
        Long totalAppointments,
        Long totalScheduled,
        Long totalCompleted,
        Long totalCancelled,
        Double cancellationRate,
        String mostActiveDoctor,
        String busiestHospital
) {}
