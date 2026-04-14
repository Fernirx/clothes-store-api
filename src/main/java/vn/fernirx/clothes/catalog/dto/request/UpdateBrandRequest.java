package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;

public record UpdateBrandRequest(
        @NullableNotBlank
        @Size(max = 100)
        String name,

        @NullableNotBlank
        String description,

        @NullableNotBlank
        @Size(max = 500)
        String logoUrl,

        @NullableNotBlank
        @Size(max = 255)
        String logoPublicId,

        Boolean isActive
) {}
