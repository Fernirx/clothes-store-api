package vn.fernirx.clothes.cart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CartResponse(
        Long cartId,
        String guestToken,
        List<CartItemResponse> items,
        BigDecimal totalAmount,
        Integer totalItems
) {}
