package vn.fernirx.clothes.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}