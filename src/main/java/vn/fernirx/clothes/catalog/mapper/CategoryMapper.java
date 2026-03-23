package vn.fernirx.clothes.catalog.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.catalog.dto.request.CategoryRequest;
import vn.fernirx.clothes.catalog.dto.response.CategoryResponse;
import vn.fernirx.clothes.catalog.entity.Category;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) return null;
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getDisplayOrder(),
                category.getIsActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        category.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        // parent is set by the service
        return category;
    }
}
