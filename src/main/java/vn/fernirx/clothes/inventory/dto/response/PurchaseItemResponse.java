package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.QualityStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseItemResponse(
        Long id,
        Long purchaseId,
        Long variantId,
        String variantSku,
        int quantityOrdered,
        int quantityReceived,
        BigDecimal unitCost,
        BigDecimal lineTotal,
        QualityStatus qualityStatus,
        int defectiveQty,
        String defectReason,
        Boolean isReceived,
        LocalDateTime receivedDate,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
