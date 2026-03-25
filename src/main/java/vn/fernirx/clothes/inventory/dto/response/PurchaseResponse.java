package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.PaymentStatus;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PurchaseResponse(
        Long id,
        Long supplierId,
        String supplierName,
        String purchaseCode,
        BigDecimal totalCost,
        PaymentStatus paymentStatus,
        PurchaseStatus status,
        String notes,
        List<PurchaseItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {}
