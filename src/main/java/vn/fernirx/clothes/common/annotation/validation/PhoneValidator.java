package vn.fernirx.clothes.common.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.fernirx.clothes.common.constant.ValidationConstants;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private boolean allowNull;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) return allowNull;
        return !value.isBlank() && value.matches(ValidationConstants.Patterns.PHONE);
    }
}
