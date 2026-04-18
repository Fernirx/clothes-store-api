package vn.fernirx.clothes.catalog.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record AssignCategoriesRequest(
        @Size(min = 1)
        Set<@NotNull Long> categoryIds
) {}
