package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product_images", indexes = {
        @Index(name = "idx_images_product_color_primary",
                columnList = "product_id, color, is_primary"),
        @Index(name = "idx_images_product_color",
                columnList = "product_id, color")})
@EntityListeners(EntityListeners.class)
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "color", nullable = false, length = 50)
    private String color;

    @Size(max = 7)
    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @Size(max = 500)
    @NotNull
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "image_public_id", nullable = false)
    private String imagePublicId;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}