package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductVariantRequest(
        @Positive
        BigDecimal price,

        @Min(0)
        Integer stockQuantity,

        @Min(5)
        Integer minStockLevel,

        @Min(0)
        Integer displayOrder,

        Boolean isActive
) {}
