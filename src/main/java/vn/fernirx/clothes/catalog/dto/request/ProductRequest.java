package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.fernirx.clothes.catalog.enums.ProductGender;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotNull(message = "Brand ID must not be null")
    private Long brandId;

    @NotBlank(message = "Product code must not be blank")
    private String code;

    @NotBlank(message = "Product slug must not be blank")
    private String slug;

    @NotBlank(message = "Product name must not be blank")
    private String name;

    private String description;

    private ProductGender gender;

    private String material;

    private String originCountry;

    @NotNull(message = "Base price must not be null")
    @DecimalMin(value = "0.0", message = "Base price must be greater than or equal to 0")
    private BigDecimal basePrice;

    private BigDecimal originalPrice;

    private BigDecimal costPrice;

    private Boolean isNew;

    private Boolean isOnSale;

    private Boolean isActive;

    private Set<Long> categoryIds;
}
