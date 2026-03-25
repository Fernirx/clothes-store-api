package vn.fernirx.clothes.catalog.dto.response;

import java.time.LocalDateTime;

public record BrandResponse(
        Long id,
        String name,
        String slug,
        String description,
        String logoUrl,
        String logoPublicId,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
