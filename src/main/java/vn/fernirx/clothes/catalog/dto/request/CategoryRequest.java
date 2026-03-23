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
public class CategoryRequest {

    @NotBlank(message = "Category name must not be blank")
    private String name;

    @NotBlank(message = "Category slug must not be blank")
    private String slug;

    private String description;

    private Long parentId;

    private Integer displayOrder;

    private Boolean isActive;
}
