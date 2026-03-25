package vn.fernirx.clothes.inventory.dto.response;

import java.time.Instant;

public record StockAdjustmentItemResponse(
        Long id,
        Long adjustmentId,
        Long variantId,
        String variantSku,
        int quantityBefore,
        int quantityAfter,
        Instant createdAt,
        Instant updatedAt
) {}
