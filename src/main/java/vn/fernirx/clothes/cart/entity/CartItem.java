package vn.fernirx.clothes.cart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_items", indexes = {
        @Index(name = "idx_cart_items_cart",
                columnList = "cart_id"),
        @Index(name = "idx_cart_items_variant",
                columnList = "variant_id")}, uniqueConstraints = {@UniqueConstraint(name = "cart_variant_UNIQUE",
        columnNames = {
                "cart_id",
                "variant_id"})})
public class CartItem extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}