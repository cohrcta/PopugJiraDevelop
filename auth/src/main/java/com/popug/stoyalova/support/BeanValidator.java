package com.popug.stoyalova.support;

import com.popug.stoyalova.exception.ValidateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeanValidator {

    private static final Validator VALIDATOR = createValidator();

    private static Validator createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public static <T> void validateOrThrow(T source) throws ValidateException {
        Set<ConstraintViolation<T>> validate = VALIDATOR.validate(source);
        if (!CollectionUtils.isEmpty(validate)) {
            throw new ValidateException(validate.stream()
                    .map(err -> ValidateException.FieldValidationError.builder()
                            .name(err.getPropertyPath().toString())
                            .constrainViolation(
                                    err.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName())
                            .message(err.getMessage())
                            .build())
                    .collect(Collectors.toUnmodifiableList()));
        }
    }

}
