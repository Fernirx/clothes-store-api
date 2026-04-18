package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import vn.fernirx.clothes.catalog.enums.ProductGender;
import vn.fernirx.clothes.common.annotation.validation.NullableNotBlank;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank
        @Size(max = 50)
        String code,

        @NotBlank
        @Size(max = 255)
        String name,

        @NotNull
        Long brandId,

        @NullableNotBlank
        String description,

        @NotNull
        ProductGender gender,

        @NullableNotBlank
        @Size(max = 200)
        String material,

        @NullableNotBlank
        @Size(max = 100)
        String originCountry,

        @NotNull
        @Positive
        BigDecimal basePrice,

        @Positive
        BigDecimal originalPrice,

        @Positive
        BigDecimal costPrice,

        @NotNull
        Boolean isNew,

        @NotNull
        Boolean isOnSale,

        @NotNull
        Boolean isActive
) {}
