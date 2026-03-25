package vn.fernirx.clothes.catalog.dto.response;

import java.time.Instant;

public record BrandResponse(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
}
