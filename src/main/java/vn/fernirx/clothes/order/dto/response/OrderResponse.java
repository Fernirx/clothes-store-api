package vn.fernirx.clothes.order.dto.response;

import vn.fernirx.clothes.order.enums.CancelledBy;
import vn.fernirx.clothes.order.enums.OrderStatus;
import vn.fernirx.clothes.order.enums.PaymentMethod;
import vn.fernirx.clothes.order.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String code,
        Long userId,
        String userEmail,
        OrderStatus status,
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        String recipientName,
        String recipientPhone,
        String shippingStreet,
        String shippingWard,
        String shippingDistrict,
        String shippingProvince,
        BigDecimal subtotal,
        BigDecimal shippingFee,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String couponCode,
        String note,
        String adminNote,
        String cancelledReason,
        CancelledBy cancelledBy,
        LocalDateTime cancelledAt,
        LocalDateTime confirmedAt,
        LocalDate estimatedDeliveryDate,
        String trackingNumber,
        LocalDateTime deliveredAt,
        LocalDateTime expiredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemResponse> orderItems,
        List<OrderStatusHistoryResponse> statusHistory
) {
}
