package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.Category;
import vn.fernirx.clothes.catalog.entity.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    boolean existsByBrandId(Long brandId);

    boolean existsByProductCategories_Category(Category category);

    @Query("""
        select distinct p
        from Product p
        left join fetch p.productCategories pc
        left join fetch pc.category
        left join fetch p.productImages
        left join fetch p.productVariants
        where p.slug = :slug and p.isActive = true
    """)
    Optional<Product> findBySlug(String slug);

    @Query("""
        select distinct p
        from Product p
        left join fetch p.productCategories pc
        left join fetch pc.category
        left join fetch p.productImages
        left join fetch p.productVariants
        where p.id = :id
    """)
    Optional<Product> findById(Long id);
}
