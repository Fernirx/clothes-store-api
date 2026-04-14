package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.request.CreateBrandRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateBrandRequest;
import vn.fernirx.clothes.catalog.dto.response.BrandResponse;
import vn.fernirx.clothes.catalog.entity.Brand;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BrandMapper {
    BrandResponse toDto(Brand brand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "products", ignore = true)
    Brand toEntity(CreateBrandRequest request);

    @BeanMapping(
            ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "logoUrl", source = "logoUrl")
    @Mapping(target = "logoPublicId", source = "logoPublicId")
    @Mapping(target = "isActive", source = "isActive")
    void updateFromRequest(UpdateBrandRequest request, @MappingTarget Brand brand);
}
