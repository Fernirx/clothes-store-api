package vn.fernirx.clothes.inventory.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.inventory.dto.request.InventoryTransactionRequest;
import vn.fernirx.clothes.inventory.dto.response.InventoryTransactionResponse;

public interface InventoryTransactionService {

    PageResponse<InventoryTransactionResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    PageResponse<InventoryTransactionResponse> getByVariantId(Long variantId, Integer page, Integer size);

    InventoryTransactionResponse getById(Long id);

    InventoryTransactionResponse create(InventoryTransactionRequest request);
}
