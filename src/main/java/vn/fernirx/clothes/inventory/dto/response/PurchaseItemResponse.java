package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.QualityStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PurchaseItemResponse(
        Long id,
        Long purchaseId,
        Long variantId,
        String variantSku,
        int quantityOrdered,
        int quantityReceived,
        BigDecimal unitCost,
        QualityStatus qualityStatus,
        Instant createdAt,
        Instant updatedAt
) {}
