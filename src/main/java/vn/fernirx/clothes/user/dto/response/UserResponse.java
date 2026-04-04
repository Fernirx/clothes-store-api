package vn.fernirx.clothes.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.enums.UserRole;

import java.time.LocalDateTime;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.User}
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        Long id,
        String email,
        Provider provider,
        String providerId,
        UserRole role,
        Boolean isVerified,
        Boolean isActive,
        LocalDateTime lastLogin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
