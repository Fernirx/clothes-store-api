package vn.fernirx.clothes.cart.service;

import vn.fernirx.clothes.cart.dto.request.AddToCartRequest;
import vn.fernirx.clothes.cart.dto.response.CartResponse;

public interface CartService {
    CartResponse addToCart(Long userId, String guestToken, AddToCartRequest request);

    CartResponse updateQuantity(Long userId, String guestToken, Long id, Integer quantity);

    CartResponse removeItem(Long userId, String guestToken, Long itemId);

    CartResponse getCart(Long userId, String guestToken);
}
