package vn.fernirx.clothes.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentResponse;
import vn.fernirx.clothes.inventory.entity.StockAdjustment;
import vn.fernirx.clothes.inventory.enums.AdjustmentStatus;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StockAdjustmentMapper {

    private final StockAdjustmentItemMapper stockAdjustmentItemMapper;

    public StockAdjustmentResponse toResponse(StockAdjustment adjustment) {
        if (adjustment == null) return null;
        return new StockAdjustmentResponse(
                adjustment.getId(),
                adjustment.getReason(),
                adjustment.getNotes(),
                adjustment.getStatus(),
                adjustment.getCreatedBy(),
                adjustment.getItems() != null
                        ? adjustment.getItems().stream()
                                .map(stockAdjustmentItemMapper::toResponse)
                                .collect(Collectors.toList())
                        : Collections.emptyList(),
                adjustment.getCreatedAt(),
                adjustment.getUpdatedAt()
        );
    }

    public StockAdjustment toEntity(StockAdjustmentRequest request) {
        if (request == null) return null;
        StockAdjustment adjustment = new StockAdjustment();
        adjustment.setReason(request.getReason());
        adjustment.setNotes(request.getNotes());
        adjustment.setStatus(request.getStatus() != null
                ? request.getStatus()
                : AdjustmentStatus.DRAFT);
        return adjustment;
    }
}
