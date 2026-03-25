package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.ReferenceType;
import vn.fernirx.clothes.inventory.enums.TransactionType;

import java.time.LocalDateTime;

public record InventoryTransactionResponse(
        Long id,
        Long variantId,
        String variantSku,
        Long createdBy,
        TransactionType type,
        int quantity,
        int oldStock,
        int newStock,
        ReferenceType referenceType,
        Long referenceId,
        String notes,
        LocalDateTime createdAt
) {}
