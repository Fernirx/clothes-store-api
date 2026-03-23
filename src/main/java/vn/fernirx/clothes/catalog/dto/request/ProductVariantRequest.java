package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {

    @NotNull(message = "Product ID must not be null")
    private Long productId;

    @NotBlank(message = "Size must not be blank")
    private String size;

    @NotBlank(message = "Color must not be blank")
    private String color;

    private String colorHex;

    private BigDecimal price;

    @NotBlank(message = "SKU must not be blank")
    private String sku;

    private Integer stockQuantity;

    private Integer minStockLevel;
}
