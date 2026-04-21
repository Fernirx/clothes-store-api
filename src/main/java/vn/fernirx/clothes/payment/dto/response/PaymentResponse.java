package vn.fernirx.clothes.payment.dto.response;

import vn.fernirx.clothes.payment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        String responseCode,
        String responseMessage,
        LocalDateTime paidAt
) {}