package vn.fernirx.clothes.cart.service;

import vn.fernirx.clothes.cart.dto.response.CartResponse;

public interface CartService {
    CartResponse getCart(Long userId, String guestToken);
}
