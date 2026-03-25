package vn.fernirx.clothes.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.fernirx.clothes.inventory.dto.request.InventoryTransactionRequest;
import vn.fernirx.clothes.inventory.dto.response.InventoryTransactionResponse;
import vn.fernirx.clothes.inventory.entity.InventoryTransaction;

@Mapper(componentModel = "spring")
public interface InventoryTransactionMapper {

    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.sku", target = "variantSku")
    InventoryTransactionResponse toResponse(InventoryTransaction transaction);

    @Mapping(target = "variant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    InventoryTransaction toEntity(InventoryTransactionRequest request);
}
