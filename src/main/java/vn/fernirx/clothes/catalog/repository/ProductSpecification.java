package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.domain.Specification;
import vn.fernirx.clothes.catalog.dto.request.ProductFilterRequest;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> hasCategory(String slug) {
        return (root, query, cb) -> {

            if (slug == null || slug.isBlank()) return null;

            return cb.equal(
                    root.join("productCategories")
                            .join("category")
                            .get("slug"),
                    slug
            );
        };
    }

    public static Specification<Product> hasBrand(String slug) {
        return (root, query, cb) ->
                slug == null ? null
                        : cb.equal(root.get("brand").get("slug"), slug);
    }

    public static Specification<Product> hasGender(ProductGender gender) {
        return (root, query, cb) ->
                gender == null ? null
                        : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null
                        : cb.greaterThanOrEqualTo(root.get("basePrice"), minPrice);
    }

    public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null
                        : cb.lessThanOrEqualTo(root.get("basePrice"), maxPrice);
    }

    public static Specification<Product> isNew(Boolean isNew) {
        return (root, query, cb) ->
                isNew == null ? null
                        : cb.equal(root.get("isNew"), isNew);
    }

    public static Specification<Product> isOnSale(Boolean isOnSale) {
        return (root, query, cb) ->
                isOnSale == null ? null
                        : cb.equal(root.get("isOnSale"), isOnSale);
    }

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String like = "%" + keyword.toLowerCase() + "%";

            return cb.like(cb.lower(root.get("name")), like);
        };
    }

    public static Specification<Product> hasActivateTrue() {
        return (root, query, cb)
                -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Product> build(ProductFilterRequest f) {
        return Specification
                .where(hasActivateTrue())
                .and(hasCategory(f.categorySlug()))
                .and(hasBrand(f.brandSlug()))
                .and(hasGender(f.gender()))
                .and(hasMinPrice(f.minPrice()))
                .and(hasMaxPrice(f.maxPrice()))
                .and(isNew(f.isNew()))
                .and(isOnSale(f.isOnSale()))
                .and(hasKeyword(f.keyword()));
    }
}
