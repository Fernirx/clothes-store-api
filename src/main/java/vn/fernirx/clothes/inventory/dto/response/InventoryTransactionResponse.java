package vn.fernirx.clothes.inventory.dto.response;

import vn.fernirx.clothes.inventory.enums.ReferenceType;
import vn.fernirx.clothes.inventory.enums.TransactionType;

import java.time.Instant;

public record InventoryTransactionResponse(
        Long id,
        Long variantId,
        String variantSku,
        TransactionType type,
        int quantity,
        int oldStock,
        int newStock,
        ReferenceType referenceType,
        Long referenceId,
        String notes,
        Instant createdAt
) {}
