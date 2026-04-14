package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;

/**
 * DTO for {@link vn.fernirx.clothes.catalog.entity.Category}
 */
public record CreateCategoryRequest(
        @NotBlank
        @Size(max = 100)
        String name,

        @NullableNotBlank
        String description,

        Integer displayOrder,

        Long parentId,

        Boolean isActive) {
}