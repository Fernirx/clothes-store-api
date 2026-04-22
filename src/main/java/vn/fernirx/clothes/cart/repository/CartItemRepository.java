package vn.fernirx.clothes.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.fernirx.clothes.cart.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndCartId(Long itemId, Long cartId);

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.variant v JOIN FETCH v.product WHERE ci.id IN :ids AND ci.cart.id = :cartId")
    List<CartItem> findAllWithVariantByIdInAndCartId(@Param("ids") List<Long> ids, @Param("cartId") Long cartId);
}