package vn.fernirx.clothes.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.fernirx.clothes.inventory.entity.StockAdjustment;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {

    Page<StockAdjustment> findAll(Pageable pageable);
}
