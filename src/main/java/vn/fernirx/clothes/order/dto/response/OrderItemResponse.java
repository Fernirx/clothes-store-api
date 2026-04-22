package vn.fernirx.clothes.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemResponse(
        Long id,
        Long variantId,
        String productCode,
        String productName,
        String variantSku,
        String variantSize,
        String variantColor,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal discountAmount,
        BigDecimal subtotal,
        LocalDateTime createdAt
) {
}
