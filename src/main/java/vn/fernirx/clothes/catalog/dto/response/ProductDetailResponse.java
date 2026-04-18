package vn.fernirx.clothes.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductDetailResponse(
        Long id,
        String name,
        String slug,
        String description,
        BigDecimal basePrice,
        BigDecimal originalPrice,
        boolean isNew,
        boolean isOnSale,
        ProductGender gender,
        String material,
        String originCountry,
        BrandInfo brand,
        List<CategoryInfo> categories,
        List<ImagesByColor> imagesByColor,
        List<VariantInfo> variants
) {
    public record BrandInfo(String name, String slug) {}

    public record CategoryInfo(String name, String slug) {}

    public record ImagesByColor(String color, String colorHex, List<ImageInfo> images) {
        public record ImageInfo(String imageUrl, boolean isPrimary) {}
    }

    public record VariantInfo(Long id, String color, String colorHex, String size, BigDecimal price, int stockQuantity) {}
}