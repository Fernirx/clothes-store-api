package vn.fernirx.clothes.catalog.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        Integer displayOrder,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
