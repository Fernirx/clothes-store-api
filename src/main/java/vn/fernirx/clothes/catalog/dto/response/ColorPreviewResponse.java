package vn.fernirx.clothes.catalog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ColorPreviewResponse(
        String color,
        String hex,
        String primaryImage
) {}
