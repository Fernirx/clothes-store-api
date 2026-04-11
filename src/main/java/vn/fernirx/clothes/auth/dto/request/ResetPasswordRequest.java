package vn.fernirx.clothes.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.StrongPassword;

public record ResetPasswordRequest(
        @NotBlank String resetPasswordToken,
        @NotBlank @Size(max = 255) @StrongPassword String password
) {}
