package vn.fernirx.clothes.payment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.payment.enums.PaymentStatus;
import vn.fernirx.clothes.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_order",
                columnList = "order_id"),
        @Index(name = "idx_payments_status",
                columnList = "status"),
        @Index(name = "idx_payments_transaction",
                columnList = "transaction_id")})
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @NotNull
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Size(max = 255)
    @Column(name = "transaction_id")
    private String transactionId;

    @Size(max = 10)
    @Column(name = "response_code", length = 10)
    private String responseCode;

    @Size(max = 255)
    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;
}