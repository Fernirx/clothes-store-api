package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.response.AdminProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.AdminProductSummaryResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductCategory;
import vn.fernirx.clothes.catalog.entity.ProductImage;

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

    AdminProductSummaryResponse toAdminProductSummaryResponse(Product product);

    @Mapping(target = "categories", source = "productCategories", qualifiedByName = "toCategoryList")
    @Mapping(target = "imagesByColor", source = "productImages", qualifiedByName = "toImageByColor")
    @Mapping(target = "variants", source = "productVariants")
    ProductDetailResponse toProductDetailResponse(Product product);

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

    // Admin
    @Mapping(target = "categories", source = "productCategories", qualifiedByName = "toAdminCategoryList")
    @Mapping(target = "imagesByColor", source = "productImages", qualifiedByName = "toAdminImageByColor")
    @Mapping(target = "variants", source = "productVariants")
    AdminProductDetailResponse toAdminProductDetailResponse(Product product);

    @Named("toAdminCategoryList")
    default List<AdminProductDetailResponse.CategoryInfo> toAdminCategoryList(Set<ProductCategory> productCategories) {
        if (productCategories == null || productCategories.isEmpty()) return List.of();
        return productCategories.stream()
                .map(pc -> new AdminProductDetailResponse.CategoryInfo(
                        pc.getCategory().getId(),
                        pc.getCategory().getName(),
                        pc.getCategory().getSlug()
                ))
                .toList();
    }

    @Named("toAdminImageByColor")
    default List<AdminProductDetailResponse.ImagesByColor> toAdminImageByColor(Set<ProductImage> productImages) {
        if (productImages == null || productImages.isEmpty()) return List.of();
        return productImages.stream()
                .collect(Collectors.groupingBy(ProductImage::getColor))
                .entrySet().stream()
                .map(entry -> new AdminProductDetailResponse.ImagesByColor(
                        entry.getKey(),
                        entry.getValue().getFirst().getColorHex(),
                        entry.getValue().stream()
                                .map(img -> new AdminProductDetailResponse.ImagesByColor.ImageInfo(
                                        img.getId(),
                                        img.getImageUrl(),
                                        img.getImagePublicId(),
                                        img.getIsPrimary()
                                ))
                                .toList()
                ))
                .toList();
    }
}
