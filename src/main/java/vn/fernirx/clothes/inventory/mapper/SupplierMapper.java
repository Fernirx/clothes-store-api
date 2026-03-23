package vn.fernirx.clothes.inventory.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.request.SupplierRequest;
import vn.fernirx.clothes.inventory.dto.response.SupplierResponse;
import vn.fernirx.clothes.inventory.entity.Supplier;

@Component
public class SupplierMapper {

    public SupplierResponse toResponse(Supplier supplier) {
        if (supplier == null) return null;
        return new SupplierResponse(
                supplier.getId(),
                supplier.getName(),
                supplier.getCode(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getIsActive(),
                supplier.getCreatedAt(),
                supplier.getUpdatedAt()
        );
    }

    public Supplier toEntity(SupplierRequest request) {
        if (request == null) return null;
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setCode(request.getCode());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        return supplier;
    }
}
