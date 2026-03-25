package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandRequest {

    @NotBlank(message = "Brand name must not be blank")
    private String name;

    @NotBlank(message = "Brand slug must not be blank")
    private String slug;

    private String description;

    private String logoUrl;

    private Boolean isActive;
}
