package vn.fernirx.clothes.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.catalog.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    boolean existsByParentId(Long parentId);

    List<Category> findByParentIsNull();

    List<Category> findByParent(Category parent);

    Optional<Category> findBySlug(String slug);
}
