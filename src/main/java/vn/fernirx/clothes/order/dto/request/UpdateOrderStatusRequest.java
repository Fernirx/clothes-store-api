package vn.fernirx.clothes.order.dto.request;

import jakarta.validation.constraints.NotNull;
import vn.fernirx.clothes.order.enums.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Trạng thái không được để trống")
        OrderStatus status,

        String adminNote
) {
}