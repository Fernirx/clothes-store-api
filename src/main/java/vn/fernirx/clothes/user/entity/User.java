package vn.fernirx.clothes.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import vn.fernirx.clothes.auth.enums.Provider;
import vn.fernirx.clothes.common.entity.BaseEntity;
import vn.fernirx.clothes.common.enums.UserRole;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_provider",
                columnList = "provider, provider_id"),
        @Index(name = "idx_users_active",
                columnList = "deleted, is_active, role")},
        uniqueConstraints = {@UniqueConstraint(name = "email_UNIQUE", columnNames = {"email"})})
@SQLDelete(sql = "UPDATE users SET deleted = true, is_active = false WHERE id = ?")
@SQLRestriction("deleted = false")
@NoArgsConstructor
@AllArgsConstructor
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
    private boolean verified = false;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
