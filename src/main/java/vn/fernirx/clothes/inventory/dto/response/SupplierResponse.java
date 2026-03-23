package vn.fernirx.clothes.inventory.dto.response;

import java.time.Instant;

public record SupplierResponse(
        Long id,
        String name,
        String code,
        String email,
        String phone,
        String address,
        Boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}
