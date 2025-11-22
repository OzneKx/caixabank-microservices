package com.caixabank.authservice.validator;

import com.caixabank.authservice.exception.InvalidEmailException;

import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException("Invalid email format");
        }
    }
}
