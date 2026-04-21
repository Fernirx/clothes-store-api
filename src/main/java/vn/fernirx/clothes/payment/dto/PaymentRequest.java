package vn.fernirx.clothes.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull
        Long orderId,

        @NotNull
        @Min(1000)
        BigDecimal amount
) {}
