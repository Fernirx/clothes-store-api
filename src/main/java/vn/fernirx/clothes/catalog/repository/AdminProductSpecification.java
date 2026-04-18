package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.domain.Specification;
import vn.fernirx.clothes.catalog.dto.request.AdminProductFilterRequest;
import vn.fernirx.clothes.catalog.entity.Product;

public class AdminProductSpecification {
    public static Specification<Product> hasCategory(Long id) {
        return (root, query, cb) -> {

            if (id == null) return null;

            return cb.equal(
                    root.join("productCategories")
                            .join("category")
                            .get("id"),
                    id
            );
        };
    }

    public static Specification<Product> hasBrand(Long id) {
        return (root, query, cb) ->
                id == null ? null
                        : cb.equal(root.get("brand").get("id"), id);
    }

    public static Specification<Product> isActive(Boolean isActive) {
        return (root, query, cb) ->
                isActive == null ? null
                        : cb.equal(root.get("isActive"), isActive);
    }

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.like(cb.lower(root.get("name")), like);
        };
    }

    public static Specification<Product> build(AdminProductFilterRequest filter) {
        return Specification
                .where(hasBrand(filter.brandId()))
                .and(hasCategory(filter.categoryId()))
                .and(isActive(filter.isActive()))
                .and(hasKeyword(filter.keyword()));
    }
}
