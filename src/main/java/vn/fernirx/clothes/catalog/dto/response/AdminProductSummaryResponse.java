package vn.fernirx.clothes.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminProductSummaryResponse(
        Long id,
        String name,
        String slug,
        String code,
        BigDecimal basePrice,
        BigDecimal originalPrice,
        BigDecimal costPrice,
        boolean isActive,
        boolean isNew,
        boolean isOnSale,
        int soldCount,
        int viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
