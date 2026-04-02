package vn.fernirx.clothes.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.common.enums.UserRole;
import vn.fernirx.clothes.inventory.entity.InventoryTransaction;
import vn.fernirx.clothes.inventory.entity.Purchase;
import vn.fernirx.clothes.inventory.entity.StockAdjustment;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_provider",
                columnList = "provider, provider_id"),
        @Index(name = "idx_users_role_active",
                columnList = "role, is_active")},
        uniqueConstraints = {@UniqueConstraint(name = "email_UNIQUE", columnNames = {"email"})})
public class User extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @Column(name = "password_hash")
    private String passwordHash;

    @NotNull
    @ColumnDefault("'LOCAL'")
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Size(max = 255)
    @Column(name = "provider_id")
    private String providerId;

    @NotNull
    @ColumnDefault("'USER'")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
