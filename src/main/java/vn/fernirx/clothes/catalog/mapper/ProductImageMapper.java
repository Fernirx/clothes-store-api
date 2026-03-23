package vn.fernirx.clothes.catalog.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.catalog.dto.request.ProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.entity.ProductImage;

@Component
public class ProductImageMapper {

    public ProductImageResponse toResponse(ProductImage image) {
        if (image == null) return null;
        return new ProductImageResponse(
                image.getId(),
                image.getProduct() != null ? image.getProduct().getId() : null,
                image.getColor(),
                image.getImageUrl(),
                image.getIsPrimary(),
                image.getCreatedAt(),
                image.getUpdatedAt()
        );
    }

    public ProductImage toEntity(ProductImageRequest request) {
        if (request == null) return null;
        ProductImage image = new ProductImage();
        image.setColor(request.getColor());
        image.setImageUrl(request.getImageUrl());
        image.setIsPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false);
        // product is set by the service
        return image;
    }
}
