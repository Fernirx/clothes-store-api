package vn.fernirx.clothes.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.cart.entity.GuestSession;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.util.LinkedHashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "guest_token", referencedColumnName = "guest_token")
    private GuestSession guestToken;
}