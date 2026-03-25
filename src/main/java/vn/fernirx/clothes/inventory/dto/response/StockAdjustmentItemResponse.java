package vn.fernirx.clothes.inventory.dto.response;

import java.time.LocalDateTime;

public record StockAdjustmentItemResponse(
        Long id,
        Long adjustmentId,
        Long variantId,
        String variantSku,
        int quantityChange,
        int quantityBefore,
        int quantityAfter,
        String note,
        LocalDateTime createdAt
) {}
