package vn.fernirx.clothes.inventory.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.inventory.dto.request.StockAdjustmentRequest;
import vn.fernirx.clothes.inventory.dto.response.StockAdjustmentResponse;

public interface StockAdjustmentService {

    PageResponse<StockAdjustmentResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    StockAdjustmentResponse getById(Long id);

    StockAdjustmentResponse create(StockAdjustmentRequest request);

    StockAdjustmentResponse update(Long id, StockAdjustmentRequest request);

    void delete(Long id);
}
