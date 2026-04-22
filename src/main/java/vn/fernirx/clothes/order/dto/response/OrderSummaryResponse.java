package vn.fernirx.clothes.order.dto.response;

import vn.fernirx.clothes.order.enums.OrderStatus;
import vn.fernirx.clothes.order.enums.PaymentMethod;
import vn.fernirx.clothes.order.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryResponse(
        Long id,
        String code,
        OrderStatus status,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        String recipientName,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
