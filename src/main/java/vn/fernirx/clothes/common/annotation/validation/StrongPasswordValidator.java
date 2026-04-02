package vn.fernirx.clothes.common.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.fernirx.clothes.common.constant.ValidationConstants;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(ValidationConstants.Patterns.PASSWORD);
    }
}
