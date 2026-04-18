package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateProductVariantRequest(
        @NotBlank
        @Size(max = 20)
        String size,

        @NotBlank
        @Size(max = 50)
        String color,

        @NotBlank
        @Size(max = 7)
        String colorHex,

        @Positive
        BigDecimal price,

        @NotBlank
        @Size(max = 100)
        String sku,

        @NotNull
        @Min(0)
        Integer stockQuantity,

        @NotNull
        @Min(5)
        Integer minStockLevel,

        @NotNull
        @Min(0)
        Integer displayOrder
) {}