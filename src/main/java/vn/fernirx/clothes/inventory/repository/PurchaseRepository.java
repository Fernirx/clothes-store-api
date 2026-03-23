package vn.fernirx.clothes.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.inventory.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsByPurchaseCode(String purchaseCode);

    Page<Purchase> findBySupplierIdOrderByCreatedAtDesc(Long supplierId, Pageable pageable);
}
