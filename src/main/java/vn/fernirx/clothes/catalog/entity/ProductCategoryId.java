package vn.fernirx.clothes.catalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1100399594927158904L;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}