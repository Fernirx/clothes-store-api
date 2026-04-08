package vn.fernirx.clothes.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.inventory.entity.PurchaseItem;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPurchaseId(Long purchaseId);

    boolean existsByVariantId(Long variantId);

    boolean existsByVariantProductId(Long productId);

    List<PurchaseItem> findByVariantId(Long variantId);
}
