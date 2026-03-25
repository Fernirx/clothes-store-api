package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.PaymentStatus;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PurchaseResponse(
        Long id,
        Long supplierId,
        String supplierName,
        Long createdBy,
        Long receivedBy,
        String purchaseCode,
        String supplierInvoiceNo,
        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal shippingCost,
        BigDecimal totalCost,
        PaymentStatus paymentStatus,
        PurchaseStatus status,
        String notes,
        String issues,
        List<PurchaseItemResponse> items,
        LocalDateTime confirmedAt,
        LocalDateTime receivedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
