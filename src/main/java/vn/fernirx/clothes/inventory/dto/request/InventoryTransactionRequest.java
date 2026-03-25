package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.fernirx.clothes.inventory.enums.ReferenceType;
import vn.fernirx.clothes.inventory.enums.TransactionType;

@Getter
@Setter
public class InventoryTransactionRequest {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private int oldStock;

    private int newStock;

    private ReferenceType referenceType;

    private Long referenceId;

    private String notes;
}
