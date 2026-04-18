package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.ProductVariant;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findAllByProductId(Long productId);

    boolean existsByProductIdAndSizeAndColor(Long productId, String size, String color);

    boolean existsBySku(String sku);

    Optional<ProductVariant> findByIdAndProductId(Long id, Long productId);
}
