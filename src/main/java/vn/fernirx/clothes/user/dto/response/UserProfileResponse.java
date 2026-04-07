package vn.fernirx.clothes.user.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.UserProfile}
 */
public record UserProfileResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String avatarUrl,
        String avatarPublicId,
        LocalDate dateOfBirth,
        String shippingName,
        String shippingPhone,
        String shippingStreet,
        String shippingWard,
        String shippingDistrict,
        String shippingProvince,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
