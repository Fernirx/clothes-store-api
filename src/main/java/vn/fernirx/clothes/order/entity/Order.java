package vn.fernirx.clothes.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.cart.entity.GuestSession;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.order.enums.PaymentStatus;
import vn.fernirx.clothes.order.enums.CancelledBy;
import vn.fernirx.clothes.order.enums.OrderStatus;
import vn.fernirx.clothes.order.enums.PaymentMethod;
import vn.fernirx.clothes.user.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_user_status",
                columnList = "user_id, status"),
        @Index(name = "idx_orders_guest",
                columnList = "guest_token"),
        @Index(name = "idx_orders_payment_status",
                columnList = "payment_status"),
        @Index(name = "idx_orders_created",
                columnList = "created_at")}, uniqueConstraints = {@UniqueConstraint(name = "code_UNIQUE",
        columnNames = {"code"})})
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "guest_token", referencedColumnName = "guest_token")
    private GuestSession guestToken;

    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @NotNull
    @ColumnDefault("'PENDING'")
    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    @ColumnDefault("'UNPAID'")
    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Size(max = 200)
    @NotNull
    @Column(name = "recipient_name", nullable = false, length = 200)
    private String recipientName;

    @Size(max = 15)
    @NotNull
    @Column(name = "recipient_phone", nullable = false, length = 15)
    private String recipientPhone;

    @Size(max = 255)
    @NotNull
    @Column(name = "shipping_street", nullable = false)
    private String shippingStreet;

    @Size(max = 100)
    @NotNull
    @Column(name = "shipping_ward", nullable = false, length = 100)
    private String shippingWard;

    @Size(max = 100)
    @NotNull
    @Column(name = "shipping_district", nullable = false, length = 100)
    private String shippingDistrict;

    @Size(max = 100)
    @NotNull
    @Column(name = "shipping_province", nullable = false, length = 100)
    private String shippingProvince;

    @NotNull
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "shipping_fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal shippingFee;

    @NotNull
    @ColumnDefault("0.00")
    @Column(name = "discount_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @NotNull
    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 50)
    @Column(name = "coupon_code", length = 50)
    private String couponCode;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Column(name = "cancelled_reason", columnDefinition = "TEST")
    private String cancelledReason;

    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "cancelled_by")
    private CancelledBy cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Size(max = 100)
    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order")
    private Set<OrderStatusHistory> orderStatusHistories = new LinkedHashSet<>();


}