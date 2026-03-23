package vn.fernirx.clothes.catalog.dto.response;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        Long parentId,
        Integer displayOrder,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
}
