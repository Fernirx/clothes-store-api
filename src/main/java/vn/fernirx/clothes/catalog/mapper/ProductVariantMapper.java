package vn.fernirx.clothes.catalog.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.catalog.dto.request.ProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.entity.ProductVariant;

@Component
public class ProductVariantMapper {

    public ProductVariantResponse toResponse(ProductVariant variant) {
        if (variant == null) return null;
        return new ProductVariantResponse(
                variant.getId(),
                variant.getProduct() != null ? variant.getProduct().getId() : null,
                variant.getSize(),
                variant.getColor(),
                variant.getColorHex(),
                variant.getPrice(),
                variant.getSku(),
                variant.getStockQuantity(),
                variant.getMinStockLevel(),
                variant.getCreatedAt(),
                variant.getUpdatedAt()
        );
    }

    public ProductVariant toEntity(ProductVariantRequest request) {
        if (request == null) return null;
        ProductVariant variant = new ProductVariant();
        variant.setSize(request.getSize());
        variant.setColor(request.getColor());
        variant.setColorHex(request.getColorHex());
        variant.setPrice(request.getPrice());
        variant.setSku(request.getSku());
        variant.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0);
        variant.setMinStockLevel(request.getMinStockLevel() != null ? request.getMinStockLevel() : 5);
        // product is set by the service
        return variant;
    }
}
