package vn.fernirx.clothes.catalog.dto.response;

import java.time.Instant;

public record ProductImageResponse(
        Long id,
        Long productId,
        String color,
        String imageUrl,
        Boolean isPrimary,
        Instant createdAt,
        Instant updatedAt
) {
}
