package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import vn.fernirx.clothes.catalog.enums.ProductGender;
import vn.fernirx.clothes.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_products_brand_active",
                columnList = "brand_id, is_active"),
        @Index(name = "idx_products_name",
                columnList = "name"),
        @Index(name = "idx_products_gender",
                columnList = "gender"),
        @Index(name = "idx_products_new_sale",
                columnList = "is_new, is_on_sale")}, uniqueConstraints = {
        @UniqueConstraint(name = "code_UNIQUE",
                columnNames = {"code"}),
        @UniqueConstraint(name = "slug_UNIQUE",
                columnNames = {"slug"})})
public class Product extends BaseEntity {
    @Size(max = 50)
    @NotNull
    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ColumnDefault("'UNISEX'")
    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "gender", nullable = false)
    private ProductGender gender;

    @Size(max = 200)
    @Column(name = "material", length = 200)
    private String material;

    @Size(max = 100)
    @Column(name = "origin_country", length = 100)
    private String originCountry;

    @NotNull
    @Column(name = "base_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "original_price", precision = 15, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_on_sale", nullable = false)
    private Boolean isOnSale;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "sold_count", nullable = false)
    private Integer soldCount;

    @ColumnDefault("'0'")
    @Column(name = "view_count", columnDefinition = "int UNSIGNED not null")
    private Integer viewCount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductCategory> productCategories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductImage> productImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<ProductVariant> productVariants = new LinkedHashSet<>();
}