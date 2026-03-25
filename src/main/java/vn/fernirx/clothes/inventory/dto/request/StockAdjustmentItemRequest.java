package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockAdjustmentItemRequest {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    private int quantityChange;

    @Min(value = 0, message = "Quantity before must be zero or greater")
    private int quantityBefore;

    @Min(value = 0, message = "Quantity after must be zero or greater")
    private int quantityAfter;

    private String note;
}
