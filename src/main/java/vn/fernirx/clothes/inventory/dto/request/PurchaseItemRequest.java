package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.fernirx.clothes.inventory.enums.QualityStatus;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseItemRequest {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    @Min(value = 1, message = "Quantity ordered must be at least 1")
    private int quantityOrdered;

    @NotNull(message = "Unit cost is required")
    private BigDecimal unitCost;

    private QualityStatus qualityStatus;
}
