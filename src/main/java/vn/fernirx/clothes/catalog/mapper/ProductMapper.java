package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.response.ColorPreviewResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductImage;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductMapper {

    @Mapping(target = "colorPreviews", source = "productImages", qualifiedByName = "toColorPreviews")
    ProductSummaryResponse toProductSummaryResponse(Product product);

    @Named("toColorPreviews")
    default List<ColorPreviewResponse> toColorPreviews(Set<ProductImage> productImages) {
        if (productImages == null || productImages.isEmpty()) {
            return List.of();
        }

        return productImages.stream()
                .filter(ProductImage::getIsPrimary)
                .map(img -> new ColorPreviewResponse(
                        img.getColor(),
                        img.getColorHex(),
                        img.getImageUrl()
                ))
                .toList();
    }
}
