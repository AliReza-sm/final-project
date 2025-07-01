package ir.maktabsharif.homeserviceprovidersystem.util;

import jakarta.validation.*;

import java.util.Set;

public class MyValidator {

    public static <T> void validate(T dto) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        StringBuilder Error = new StringBuilder();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<T> violation : violations) {
                Error.append("___Error: ").append(violation.getMessage());
            }
            throw new ConstraintViolationException(Error.toString(),violations);
        }
    }

}
