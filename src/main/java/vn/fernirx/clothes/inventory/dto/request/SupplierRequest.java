package vn.fernirx.clothes.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierRequest {

    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Supplier code is required")
    private String code;

    private String email;

    private String phone;

    private String address;

    private Boolean isActive;
}
