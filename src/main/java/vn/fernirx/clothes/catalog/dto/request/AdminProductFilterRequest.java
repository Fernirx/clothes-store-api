package vn.fernirx.clothes.catalog.dto.request;

public record AdminProductFilterRequest(
        Long categoryId,
        Long brandId,
        Boolean isActive,
        String keyword
) {}
