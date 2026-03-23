package vn.fernirx.clothes.inventory.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.request.PurchaseItemRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseItemResponse;
import vn.fernirx.clothes.inventory.entity.PurchaseItem;
import vn.fernirx.clothes.inventory.enums.QualityStatus;

@Component
public class PurchaseItemMapper {

    public PurchaseItemResponse toResponse(PurchaseItem item) {
        if (item == null) return null;
        return new PurchaseItemResponse(
                item.getId(),
                item.getPurchase() != null ? item.getPurchase().getId() : null,
                item.getVariant() != null ? item.getVariant().getId() : null,
                item.getVariant() != null ? item.getVariant().getSku() : null,
                item.getQuantityOrdered(),
                item.getQuantityReceived(),
                item.getUnitCost(),
                item.getQualityStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    public PurchaseItem toEntity(PurchaseItemRequest request) {
        if (request == null) return null;
        PurchaseItem item = new PurchaseItem();
        item.setQuantityOrdered(request.getQuantityOrdered());
        item.setUnitCost(request.getUnitCost());
        item.setQualityStatus(request.getQualityStatus() != null
                ? request.getQualityStatus()
                : QualityStatus.PENDING);
        // purchase and variant are set by the service
        return item;
    }
}
