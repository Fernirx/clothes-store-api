package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByBrandId(Long brandId);

    boolean existsByProductCategories_Category(Category category);

    Page<Product> findByIsActiveTrue(Pageable pageable);
}
