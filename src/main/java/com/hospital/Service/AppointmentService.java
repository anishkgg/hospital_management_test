package com.hospital.Service;

import com.hospital.dto.requestDto.AppointmentRequestDTO;
import com.hospital.dto.responseDto.AppointmentBookingResponseDTO;
import com.hospital.dto.responseDto.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {
    AppointmentBookingResponseDTO bookAppointment(AppointmentRequestDTO appointmentRequestDTO);
    AppointmentResponseDTO getAppointmentByBookingCode(String bookingCode);
    List<AppointmentResponseDTO> getAllAppointment();
    AppointmentResponseDTO cancelAppointment(Long appointmentId);
    AppointmentResponseDTO completeAppointment(Long appointmentId);
}
