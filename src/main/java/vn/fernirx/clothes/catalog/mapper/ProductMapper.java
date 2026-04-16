package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.response.ProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductCategory;
import vn.fernirx.clothes.catalog.entity.ProductImage;
import vn.fernirx.clothes.catalog.entity.ProductVariant;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductMapper {

    @Mapping(target = "colorPreviews", source = "productImages", qualifiedByName = "toColorPreviews")
    ProductSummaryResponse toProductSummaryResponse(Product product);

    @Mapping(target = "categories", source = "productCategories", qualifiedByName = "toCategoryList")
    @Mapping(target = "imagesByColor", source = "productImages", qualifiedByName = "toImageByColor")
    @Mapping(target = "variants", source = "productVariants", qualifiedByName = "toVariantList")
    ProductDetailResponse toProductDetailResponse(Product product);

    ProductDetailResponse.VariantInfo toVariantInfo(ProductVariant variant);

    @Named("toColorPreviews")
    default List<ProductSummaryResponse.ColorPreviewResponse> toColorPreviews(Set<ProductImage> productImages) {
        if (productImages == null || productImages.isEmpty()) {
            return List.of();
        }

        return productImages.stream()
                .filter(ProductImage::getIsPrimary)
                .map(img -> new ProductSummaryResponse.ColorPreviewResponse(
                        img.getColor(),
                        img.getColorHex(),
                        img.getImageUrl()
                ))
                .toList();
    }

    @Named("toCategoryList")
    default List<ProductDetailResponse.CategoryInfo> toCategoryList(Set<ProductCategory> productCategories) {
        if (productCategories == null || productCategories.isEmpty()) {
            return List.of();
        }

        return productCategories.stream()
                .map(pc -> new ProductDetailResponse.CategoryInfo(
                        pc.getCategory().getName(),
                        pc.getCategory().getSlug()
                ))
                .toList();
    }

    @Named("toImageByColor")
    default List<ProductDetailResponse.ImagesByColor> toImageByColor(Set<ProductImage> productImages) {
        if (productImages == null || productImages.isEmpty()) {
            return List.of();
        }

        return productImages.stream()
                .collect(Collectors.groupingBy(ProductImage::getColor))
                .entrySet().stream()
                .map(entry -> new ProductDetailResponse.ImagesByColor(
                        entry.getKey(),
                        entry.getValue().getFirst().getColorHex(),
                        entry.getValue().stream()
                                .map(img -> new ProductDetailResponse.ImagesByColor.ImageInfo(
                                        img.getImageUrl(),
                                        img.getIsPrimary()
                                ))
                                .toList()
                ))
                .toList();
    }

    @Named("toVariantList")
    default List<ProductDetailResponse.VariantInfo> toVariantList(Set<ProductVariant> productVariants) {
        if (productVariants == null || productVariants.isEmpty()) {
            return List.of();
        }

        return productVariants.stream()
                .map(this::toVariantInfo)
                .toList();
    }
}
