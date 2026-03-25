package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;
import vn.fernirx.clothes.inventory.enums.AdjustmentType;

import java.util.List;

@Getter
@Setter
public class StockAdjustmentRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotNull(message = "Type is required")
    private AdjustmentType type;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String notes;

    private AdjustmentStatus status;

    @Valid
    private List<StockAdjustmentItemRequest> items;
}
