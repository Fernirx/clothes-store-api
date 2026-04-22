package vn.fernirx.clothes.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.order.entity.OrderStatusHistory;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> {
}
