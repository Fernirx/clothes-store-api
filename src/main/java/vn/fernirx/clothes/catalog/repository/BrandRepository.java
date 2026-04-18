package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.Brand;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);

    Optional<Brand> findBySlug(String slug);

    Page<Brand> findAllByIsActiveTrue(Pageable pageable);
}
