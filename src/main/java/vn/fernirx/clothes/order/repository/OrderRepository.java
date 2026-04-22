package vn.fernirx.clothes.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fernirx.clothes.order.entity.Order;
import vn.fernirx.clothes.order.enums.OrderStatus;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.variant " +
           "LEFT JOIN FETCH o.orderStatusHistories WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
}
