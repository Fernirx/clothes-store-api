package vn.fernirx.clothes.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fernirx.clothes.inventory.dto.request.PurchaseItemRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseItemResponse;
import vn.fernirx.clothes.inventory.entity.PurchaseItem;
import vn.fernirx.clothes.inventory.enums.QualityStatus;

@Mapper(componentModel = "spring")
public interface PurchaseItemMapper {

    @Mapping(source = "purchase.id", target = "purchaseId")
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.sku", target = "variantSku")
    PurchaseItemResponse toResponse(PurchaseItem item);

    @Mapping(target = "purchase", ignore = true)
    @Mapping(target = "variant", ignore = true)
    @Mapping(target = "quantityReceived", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "qualityStatus", defaultExpression = "java(QualityStatus.PENDING)")
    PurchaseItem toEntity(PurchaseItemRequest request);
}
