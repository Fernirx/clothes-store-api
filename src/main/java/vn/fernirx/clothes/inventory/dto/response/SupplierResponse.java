package vn.fernirx.clothes.inventory.dto.response;

import java.time.LocalDateTime;

public record SupplierResponse(
        Long id,
        String name,
        String code,
        String email,
        String phone,
        String contactPerson,
        String address,
        String notes,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
