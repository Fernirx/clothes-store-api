package vn.fernirx.clothes.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartItemResponse(
        Long id,
        Long variantId,
        String productSlug,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal
) {}
