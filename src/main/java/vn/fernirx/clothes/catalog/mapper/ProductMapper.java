package vn.fernirx.clothes.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import vn.fernirx.clothes.catalog.dto.request.ProductRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductResponse;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "brand.id", target = "brandId")
    @Mapping(source = "brand.name", target = "brandName")
    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "categoriesToIds")
    ProductResponse toResponse(Product product);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "soldCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isNew", defaultExpression = "java(Boolean.FALSE)")
    @Mapping(target = "isOnSale", defaultExpression = "java(Boolean.FALSE)")
    @Mapping(target = "isActive", defaultExpression = "java(Boolean.TRUE)")
    Product toEntity(ProductRequest request);

    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "soldCount", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isNew", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isOnSale", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isActive", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(ProductRequest request, @MappingTarget Product product);

    @Named("categoriesToIds")
    default Set<Long> categoriesToIds(Set<Category> categories) {
        if (categories == null) return Set.of();
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }
}
