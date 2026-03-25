package vn.fernirx.clothes.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentItemRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentItemResponse;
import vn.fernirx.clothes.inventory.entity.StockAdjustmentItem;

@Mapper(componentModel = "spring")
public interface StockAdjustmentItemMapper {

    @Mapping(source = "adjustment.id", target = "adjustmentId")
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.sku", target = "variantSku")
    StockAdjustmentItemResponse toResponse(StockAdjustmentItem item);

    @Mapping(target = "adjustment", ignore = true)
    @Mapping(target = "variant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    StockAdjustmentItem toEntity(StockAdjustmentItemRequest request);
}
