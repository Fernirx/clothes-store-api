package vn.fernirx.clothes.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.constant.ValidationConstants;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link vn.fernirx.clothes.user.entity.UserProfile}
 */
public record UpdateProfileRequest(
        @Size(max = 100)
        @Pattern(
                regexp = ValidationConstants.Patterns.NOT_BLANK_PATTERN,
                message = "must not be blank"
        )
        String firstName,
        @Size(max = 100)
        @Pattern(
                regexp = ValidationConstants.Patterns.NOT_BLANK_PATTERN,
                message = "must not be blank"
        )
        String lastName,
        LocalDate dateOfBirth) {
}