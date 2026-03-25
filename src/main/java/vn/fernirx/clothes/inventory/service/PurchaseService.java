package vn.fernirx.clothes.inventory.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.inventory.dto.request.PurchaseRequest;
import vn.fernirx.clothes.inventory.dto.response.PurchaseResponse;

public interface PurchaseService {

    PageResponse<PurchaseResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    PurchaseResponse getById(Long id);

    PurchaseResponse create(PurchaseRequest request);

    PurchaseResponse update(Long id, PurchaseRequest request);

    void delete(Long id);
}
