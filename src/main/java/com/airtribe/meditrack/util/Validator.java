package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exceptions.InvalidDataException;

public class Validator {

    private Validator() {
        throw new AssertionError("Validator is a utility class and cannot be instantiated");
    }

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Name cannot exceed 100 characters");
        }
    }

    public static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        if (age > 150) {
            throw new IllegalArgumentException("Age cannot exceed 150");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        String phoneRegex = "^[\\d\\s\\-\\(\\)]{10,15}$";
        if (!phoneNumber.replaceAll("[\\s\\-\\(\\)]", "").matches("^\\d{10,15}$")) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
    }

    public static void validateFee(double fee) {
        if (fee < 0) {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
        if (fee > 100000) {
            throw new IllegalArgumentException("Consultation fee cannot exceed 100,000");
        }
    }

    public static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be a positive number");
        }
    }
}

