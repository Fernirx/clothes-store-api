package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.catalog.enums.ProductGender;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NullableNotBlank
        @Size(max = 255)
        String name,

        @NullableNotBlank
        String description,

        @NullableNotBlank
        @Size(max = 200)
        String material,

        @NullableNotBlank
        @Size(max = 100)
        String originCountry,

        ProductGender gender,

        @Positive
        BigDecimal basePrice,

        @Positive
        BigDecimal originalPrice,

        @Positive
        BigDecimal costPrice,

        Boolean isNew,
        Boolean isOnSale,
        Boolean isActive
) {}
