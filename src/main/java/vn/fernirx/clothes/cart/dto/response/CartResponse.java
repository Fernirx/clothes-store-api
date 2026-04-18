package vn.fernirx.clothes.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartItemResponse> items,
        BigDecimal totalAmount,
        Integer totalItems
) {}
