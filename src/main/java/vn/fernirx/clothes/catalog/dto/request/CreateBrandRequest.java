package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link vn.fernirx.clothes.catalog.entity.Brand}
 */
public record CreateBrandRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        String description,

        @Size(max = 500)
        String logoUrl,

        @Size(max = 255)
        String logoPublicId,

        boolean isActive) {
}