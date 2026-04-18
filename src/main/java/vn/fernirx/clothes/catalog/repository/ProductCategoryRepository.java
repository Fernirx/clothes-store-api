package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.catalog.entity.ProductCategory;
import vn.fernirx.clothes.catalog.entity.ProductCategoryId;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryId> {
    void deleteByProductId(Long productId);
}