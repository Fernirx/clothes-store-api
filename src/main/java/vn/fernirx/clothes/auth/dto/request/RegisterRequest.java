package vn.fernirx.clothes.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;
import vn.fernirx.clothes.common.annotation.validation.StrongPassword;

public record RegisterRequest(
        @NotBlank
        @Size(max = 100)
        @Email
        String email,

        @NotBlank
        @Size(max = 255)
        @StrongPassword
        String password,

        @NullableNotBlank
        @Size(max = 100)
        String firstName,

        @NullableNotBlank
        @Size(max = 100)
        String lastName
) {}
