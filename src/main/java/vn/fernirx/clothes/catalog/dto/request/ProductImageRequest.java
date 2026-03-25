package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageRequest {

    @NotNull(message = "Product ID must not be null")
    private Long productId;

    private String color;

    @NotBlank(message = "Image URL must not be blank")
    private String imageUrl;

    private Boolean isPrimary;
}
