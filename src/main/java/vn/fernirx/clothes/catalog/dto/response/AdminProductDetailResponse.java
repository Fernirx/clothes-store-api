package vn.fernirx.clothes.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AdminProductDetailResponse(
        Long id,
        String name,
        String slug,
        String code,
        String description,
        BigDecimal basePrice,
        BigDecimal originalPrice,
        BigDecimal costPrice,
        boolean isActive,
        boolean isNew,
        boolean isOnSale,
        ProductGender gender,
        String material,
        String originCountry,
        int soldCount,
        int viewCount,
        BrandInfo brand,
        List<CategoryInfo> categories,
        List<ImagesByColor> imagesByColor,
        List<VariantInfo> variants,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record BrandInfo(Long id, String name, String slug) {}

    public record CategoryInfo(Long id, String name, String slug) {}

    public record ImagesByColor(String color, String colorHex, List<ImageInfo> images) {
        public record ImageInfo(Long id, String imageUrl, String publicId, boolean isPrimary) {}
    }

    public record VariantInfo(
            Long id,
            String color,
            String colorHex,
            String size,
            String sku,
            BigDecimal price,
            int stockQuantity,
            int minStockLevel,
            boolean isActive
    ) {}
}
