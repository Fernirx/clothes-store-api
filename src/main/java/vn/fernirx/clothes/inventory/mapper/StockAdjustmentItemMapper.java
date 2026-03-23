package vn.fernirx.clothes.inventory.mapper;

import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentItemRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentItemResponse;
import vn.fernirx.clothes.inventory.entity.StockAdjustmentItem;

@Component
public class StockAdjustmentItemMapper {

    public StockAdjustmentItemResponse toResponse(StockAdjustmentItem item) {
        if (item == null) return null;
        return new StockAdjustmentItemResponse(
                item.getId(),
                item.getAdjustment() != null ? item.getAdjustment().getId() : null,
                item.getVariant() != null ? item.getVariant().getId() : null,
                item.getVariant() != null ? item.getVariant().getSku() : null,
                item.getQuantityBefore(),
                item.getQuantityAfter(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    public StockAdjustmentItem toEntity(StockAdjustmentItemRequest request) {
        if (request == null) return null;
        StockAdjustmentItem item = new StockAdjustmentItem();
        item.setQuantityBefore(request.getQuantityBefore());
        item.setQuantityAfter(request.getQuantityAfter());
        // adjustment and variant are set by the service
        return item;
    }
}
