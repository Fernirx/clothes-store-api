package vn.fernirx.clothes.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.constant.ValidationConstants;

public record VerifyOtpRequest(
        @NotBlank
        @Size(max = 100)
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = ValidationConstants.Patterns.OTP)
        String otp
) {}
