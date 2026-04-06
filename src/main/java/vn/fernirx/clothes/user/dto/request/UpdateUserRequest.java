package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.enums.UserRole;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.User}
 */
public record UpdateUserRequest(
        @Size(max = 100) @Email String email,
        UserRole role,
        Boolean active,
        Boolean verified) {
}
