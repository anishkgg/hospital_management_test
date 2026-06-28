package com.hospital.Utils;

import com.hospital.repository.AppointmentRepository;

import java.security.SecureRandom;

public class AppointmentUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 10;

    private AppointmentUtils() {

    }

    public static String generateUniqueBookingCode(AppointmentRepository appointmentRepository) {
        String code;

        do {
            StringBuilder stringBuilder = new StringBuilder(CODE_LENGTH);
            for(int i = 0; i < CODE_LENGTH; i++) {
                stringBuilder.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
            }
            code = stringBuilder.toString();
        } while(appointmentRepository.existsByBookingCode(code));

        return code;
    }
}
