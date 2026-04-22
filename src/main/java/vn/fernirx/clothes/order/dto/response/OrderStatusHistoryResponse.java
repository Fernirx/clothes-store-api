package vn.fernirx.clothes.order.dto.response;

import vn.fernirx.clothes.order.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponse(
        Long id,
        OrderStatus oldStatus,
        OrderStatus newStatus,
        Long changedById,
        String changedByName,
        LocalDateTime createdAt
) {
}
