package vn.fernirx.clothes.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank String refreshToken
) {}