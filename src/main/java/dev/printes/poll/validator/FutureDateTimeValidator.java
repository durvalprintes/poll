package dev.printes.poll.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FutureDateTimeValidator implements ConstraintValidator<ValidFutureDateTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        try {
            LocalDateTime parsedDate = LocalDateTime.parse(value, FORMATTER);
            return !parsedDate.isBefore(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

