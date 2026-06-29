package com.hospital.Utils;

import java.util.regex.Pattern;

public final class ValidationUtils {

    // Regex for Indian Phone Number:
    // Optional prefix +91, 91, or 0, followed by 10 digits starting with 6, 7, 8, or 9.
    // Allows optional space or hyphen after the country code.
    private static final String INDIAN_PHONE_REGEX = "^(?:\\+91[-\\s]?|91[-\\s]?|0)?[6-9]\\d{9}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(INDIAN_PHONE_REGEX);

    // Regex for standard Email validation:
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private ValidationUtils() {
    }

    public static boolean isValidIndianPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
