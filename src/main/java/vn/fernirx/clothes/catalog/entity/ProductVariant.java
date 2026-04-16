package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_variants_product_order",
                columnList = "product_id, display_order"),
        @Index(name = "idx_variants_product_active",
                columnList = "product_id, is_active"),
        @Index(name = "idx_variants_stock",
                columnList = "stock_quantity")}, uniqueConstraints = {
        @UniqueConstraint(name = "variant_UNIQUE",
                columnNames = {
                        "product_id",
                        "size",
                        "color"}),
        @UniqueConstraint(name = "sku_UNIQUE",
                columnNames = {"sku"})})
public class ProductVariant extends BaseEntity {
    @Size(max = 20)
    @NotNull
    @Column(name = "size", nullable = false, length = 20)
    private String size;

    @Size(max = 50)
    @NotNull
    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Size(max = 7)
    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price;

    @Size(max = 100)
    @NotNull
    @Column(name = "sku", nullable = false, length = 100)
    private String sku;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @NotNull
    @ColumnDefault("5")
    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}