package vn.fernirx.clothes.cart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.fernirx.clothes.order.entity.Order;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "guest_sessions", indexes = {@Index(name = "idx_guest_sessions_expires",
        columnList = "expires_at")}, uniqueConstraints = {@UniqueConstraint(name = "guest_token_UNIQUE",
        columnNames = {"guest_token"})})
@EntityListeners(AuditingEntityListener.class)
public class GuestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 64)
    @NotNull
    @Column(name = "guest_token", nullable = false, length = 64)
    private String guestToken;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_active", nullable = false)
    private LocalDateTime lastActive;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne(mappedBy = "guestToken")
    private Cart cart;

    @OneToMany(mappedBy = "guestToken")
    private Set<Order> orders = new LinkedHashSet<>();
}