package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.*;
import vn.fernirx.clothes.catalog.dto.request.CreateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateCategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.catalog.entity.Category;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CategoryMapper {
    @Mapping(source = "parent.id", target = "parentId")
    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", defaultExpression = "java(Boolean.TRUE)")
    Category toEntity(CreateCategoryRequest request);

    @BeanMapping(
            ignoreByDefault = true,
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "displayOrder", source = "displayOrder")
    @Mapping(target = "isActive", source = "isActive")
    void updateFromRequest(UpdateCategoryRequest request, @MappingTarget Category category);
}
