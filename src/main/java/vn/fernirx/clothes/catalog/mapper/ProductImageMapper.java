package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import vn.fernirx.clothes.catalog.dto.request.CreateProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.entity.ProductImage;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ProductImageMapper {
    ProductImageResponse toResponse(ProductImage productImage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProductImage toEntity(CreateProductImageRequest createProductImageRequest);
}
