package vn.fernirx.clothes.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductSummaryResponse(
        String name,
        String slug,
        BigDecimal basePrice,
        BigDecimal originalPrice,
        Boolean isNew,
        Boolean isOnSale,
        List<ColorPreviewResponse> colorPreviews
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ColorPreviewResponse(
            String color,
            String hex,
            String primaryImage
    ) {}
}
