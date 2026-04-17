package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductImageRequest(
        @NotBlank
        @Size(max = 50)
        String color,

        @NotBlank
        @Size(max = 7)
        String colorHex,

        @NotBlank
        @Size(max = 500)
        String imageUrl,

        @NotBlank
        @Size(max = 255)
        String imagePublicId,

        @NotNull
        Boolean isPrimary
) {}
