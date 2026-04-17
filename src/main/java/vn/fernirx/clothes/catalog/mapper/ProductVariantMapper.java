package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.request.CreateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.entity.ProductVariant;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface ProductVariantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "product", ignore = true)
    ProductVariant toEntity(CreateProductVariantRequest createProductVariantRequest);

    ProductVariantResponse toResponse(ProductVariant productVariant);

    @BeanMapping(
            ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "price", source = "price")
    @Mapping(target = "stockQuantity", source = "stockQuantity")
    @Mapping(target = "minStockLevel", source = "minStockLevel")
    @Mapping(target = "displayOrder", source = "displayOrder")
    @Mapping(target = "isActive", source = "isActive")
    void updateFromRequest(UpdateProductVariantRequest request, @MappingTarget ProductVariant entity);
}