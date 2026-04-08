package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.StrongPassword;

public record ChangePasswordRequest(
        @NotBlank
        @Size(max = 255)
        @StrongPassword
        String oldPassword,

        @NotBlank
        @Size(max = 255)
        @StrongPassword
        String newPassword
) {}
