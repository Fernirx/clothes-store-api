package vn.fernirx.clothes.catalog.dto.request;

import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;

public record ProductFilterRequest(
        String categorySlug,
        String brandSlug,
        ProductGender gender,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean isNew,
        Boolean isOnSale,
        String keyword
) {}
