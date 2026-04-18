package vn.fernirx.clothes.catalog.dto.response;

import java.time.LocalDateTime;

public record ProductImageResponse(
        Long id,
        String color,
        String colorHex,
        String imageUrl,
        String imagePublicId,
        Boolean isPrimary,
        LocalDateTime createdAt
) {}
