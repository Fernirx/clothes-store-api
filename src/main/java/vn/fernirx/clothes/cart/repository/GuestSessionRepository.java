package vn.fernirx.clothes.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.fernirx.clothes.cart.entity.GuestSession;

public interface GuestSessionRepository extends JpaRepository<GuestSession, Long> {
}