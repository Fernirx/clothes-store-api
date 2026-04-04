package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.StrongPassword;
import vn.fernirx.clothes.common.enums.UserRole;

import java.io.Serializable;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.User}
 */
public record CreateUserRequest(
        @NotBlank @Size(max = 100) @Email String email,
        @NotBlank @Size(max = 255) @StrongPassword String password,
        @NotNull UserRole role,
        @NotNull Boolean verified,
        @NotNull Boolean active) {
}