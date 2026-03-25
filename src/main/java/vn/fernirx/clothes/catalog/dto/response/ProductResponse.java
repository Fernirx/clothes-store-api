package vn.fernirx.clothes.catalog.dto.response;

import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record ProductResponse(
        Long id,
        Long brandId,
        String brandName,
        String code,
        String slug,
        String name,
        String description,
        ProductGender gender,
        String material,
        String originCountry,
        BigDecimal basePrice,
        BigDecimal originalPrice,
        BigDecimal costPrice,
        Boolean isNew,
        Boolean isOnSale,
        Boolean isActive,
        Integer soldCount,
        Integer viewCount,
        Set<Long> categoryIds,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
