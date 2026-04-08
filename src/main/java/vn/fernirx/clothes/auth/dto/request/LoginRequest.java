package vn.fernirx.clothes.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.StrongPassword;

public record LoginRequest(
        @NotBlank @Size(max = 100) @Email String email,
        @NotBlank @Size(max = 255) @StrongPassword String password
) {}
