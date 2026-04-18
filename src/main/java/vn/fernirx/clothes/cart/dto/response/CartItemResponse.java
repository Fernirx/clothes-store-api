package vn.fernirx.clothes.cart.dto.response;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long variantId,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {}
