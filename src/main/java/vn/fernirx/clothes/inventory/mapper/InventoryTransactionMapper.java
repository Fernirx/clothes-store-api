package vn.fernirx.clothes.inventory.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.response.InventoryTransactionResponse;
import vn.fernirx.clothes.inventory.entity.InventoryTransaction;

@Component
public class InventoryTransactionMapper {

    public InventoryTransactionResponse toResponse(InventoryTransaction transaction) {
        if (transaction == null) return null;
        return new InventoryTransactionResponse(
                transaction.getId(),
                transaction.getVariant() != null ? transaction.getVariant().getId() : null,
                transaction.getVariant() != null ? transaction.getVariant().getSku() : null,
                transaction.getType(),
                transaction.getQuantity(),
                transaction.getOldStock(),
                transaction.getNewStock(),
                transaction.getReferenceType(),
                transaction.getReferenceId(),
                transaction.getNotes(),
                transaction.getCreatedAt()
        );
    }
}
