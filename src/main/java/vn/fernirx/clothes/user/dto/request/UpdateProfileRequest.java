package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;

import java.time.LocalDate;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.UserProfile}
 */
public record UpdateProfileRequest(
        @NullableNotBlank
        @Size(max = 100)
        String firstName,

        @NullableNotBlank
        @Size(max = 100)
        String lastName,

        @Past
        LocalDate dateOfBirth
) {}
