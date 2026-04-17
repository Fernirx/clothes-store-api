package vn.fernirx.clothes.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByVariant_ProductId(Long productId);
}