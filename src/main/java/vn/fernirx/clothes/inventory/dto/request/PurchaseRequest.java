package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.fernirx.clothes.inventory.enums.PaymentStatus;
import vn.fernirx.clothes.inventory.enums.PurchaseStatus;

import java.util.List;

@Getter
@Setter
public class PurchaseRequest {

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotBlank(message = "Purchase code is required")
    private String purchaseCode;

    private String notes;

    private PaymentStatus paymentStatus;

    private PurchaseStatus status;

    @Valid
    private List<PurchaseItemRequest> items;
}
