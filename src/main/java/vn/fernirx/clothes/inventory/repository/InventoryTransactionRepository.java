package vn.fernirx.clothes.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.inventory.entity.InventoryTransaction;
import vn.fernirx.clothes.inventory.enums.ReferenceType;

import java.util.List;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

    Page<InventoryTransaction> findByVariantIdOrderByCreatedAtDesc(Long variantId, Pageable pageable);

    List<InventoryTransaction> findByReferenceTypeAndReferenceId(ReferenceType referenceType, Long referenceId);
}
