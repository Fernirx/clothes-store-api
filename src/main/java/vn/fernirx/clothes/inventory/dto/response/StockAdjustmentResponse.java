package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;
import vn.fernirx.clothes.inventory.enums.AdjustmentType;

import java.time.LocalDateTime;
import java.util.List;

public record StockAdjustmentResponse(
        Long id,
        Long createdBy,
        String code,
        AdjustmentType type,
        AdjustmentStatus status,
        String reason,
        String notes,
        List<StockAdjustmentItemResponse> items,
        LocalDateTime confirmedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
