package vn.fernirx.clothes.catalog.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductVariantResponse(
        Long id,
        String size,
        String color,
        String colorHex,
        BigDecimal price,
        String sku,
        Integer stockQuantity,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
