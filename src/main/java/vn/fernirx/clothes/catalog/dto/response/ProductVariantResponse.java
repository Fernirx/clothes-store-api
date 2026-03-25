package vn.fernirx.clothes.catalog.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductVariantResponse(
        Long id,
        Long productId,
        String size,
        String color,
        String colorHex,
        BigDecimal price,
        String sku,
        Integer stockQuantity,
        Integer minStockLevel,
        Instant createdAt,
        Instant updatedAt
) {
}
