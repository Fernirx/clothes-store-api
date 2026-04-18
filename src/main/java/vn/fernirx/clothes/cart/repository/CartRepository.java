package vn.fernirx.clothes.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}