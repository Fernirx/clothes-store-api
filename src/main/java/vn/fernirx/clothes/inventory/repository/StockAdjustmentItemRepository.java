package vn.fernirx.clothes.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.inventory.entity.StockAdjustmentItem;

import java.util.List;

@Repository
public interface StockAdjustmentItemRepository extends JpaRepository<StockAdjustmentItem, Long> {

    List<StockAdjustmentItem> findByAdjustmentId(Long adjustmentId);
}
