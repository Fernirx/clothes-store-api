package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsByCode(String code);

    boolean existsByBrandId(Long brandId);

    boolean existsByCategoriesId(Long categoryId);

    Page<Product> findByIsActiveTrue(Pageable pageable);
}
