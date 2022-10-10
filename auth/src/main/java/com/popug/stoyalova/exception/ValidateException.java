package com.popug.stoyalova.exception;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class ValidateException extends Exception {

    private final transient List<FieldValidationError> fieldValidationErrors;

    @Override
    public String getMessage() {
        return fieldValidationErrors.stream()
                .map(FieldValidationError::toString)
                .collect(Collectors.joining(";"));
    }

    @ToString
    @EqualsAndHashCode
    @Builder
    @Getter
    public static class FieldValidationError {

        private final String name;
        private final String message;
        private final String constrainViolation;
    }
}
