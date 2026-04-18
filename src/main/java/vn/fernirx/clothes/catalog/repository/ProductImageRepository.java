package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.ProductImage;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    @Modifying
    @Query("""
        UPDATE ProductImage pi
        SET pi.isPrimary = false
        WHERE pi.product.id = :productId
        AND pi.color = :color
    """)
    void resetImagePrimary(Long productId, String color);

    Optional<ProductImage> findByIdAndProductId(Long id, Long productId);
}
