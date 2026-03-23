package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;

import java.time.Instant;
import java.util.List;

public record StockAdjustmentResponse(
        Long id,
        String reason,
        String notes,
        AdjustmentStatus status,
        Long createdBy,
        List<StockAdjustmentItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {}
