package vn.fernirx.clothes.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.cart.entity.CartItem;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCartId(Long itemId, Long cartId);
}