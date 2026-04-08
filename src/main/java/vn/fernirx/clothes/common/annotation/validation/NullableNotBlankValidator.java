package vn.fernirx.clothes.common.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullableNotBlankValidator implements ConstraintValidator<NullableNotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.isBlank();
    }
}
