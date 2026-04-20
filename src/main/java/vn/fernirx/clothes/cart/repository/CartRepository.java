package vn.fernirx.clothes.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.cart.entity.Cart;
import vn.fernirx.clothes.cart.entity.GuestSession;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findByGuestSession(GuestSession guestSession);

    Optional<Cart> findByGuestSession_GuestToken(String guestToken);
}