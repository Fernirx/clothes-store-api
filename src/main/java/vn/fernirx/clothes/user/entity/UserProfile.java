package vn.fernirx.clothes.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_profiles_name",
                columnList = "last_name, first_name"),
        @Index(name = "idx_profiles_shipping_phone",
                columnList = "shipping_phone")}, uniqueConstraints = {@UniqueConstraint(name = "user_id_UNIQUE",
        columnNames = {"user_id"})})
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile extends BaseEntity {
    @Size(max = 100)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    private String lastName;

    @Size(max = 500)
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Size(max = 255)
    @Column(name = "avatar_public_id")
    private String avatarPublicId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 200)
    @Column(name = "shipping_name", length = 200)
    private String shippingName;

    @Size(max = 15)
    @Column(name = "shipping_phone", length = 15)
    private String shippingPhone;

    @Size(max = 255)
    @Column(name = "shipping_street")
    private String shippingStreet;

    @Size(max = 100)
    @Column(name = "shipping_ward", length = 100)
    private String shippingWard;

    @Size(max = 100)
    @Column(name = "shipping_district", length = 100)
    private String shippingDistrict;

    @Size(max = 100)
    @Column(name = "shipping_province", length = 100)
    private String shippingProvince;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}