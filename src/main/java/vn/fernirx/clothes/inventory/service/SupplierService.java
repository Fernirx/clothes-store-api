package vn.fernirx.clothes.inventory.service;

import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.inventory.dto.request.SupplierRequest;
import vn.fernirx.clothes.inventory.dto.response.SupplierResponse;

public interface SupplierService {

    PageResponse<SupplierResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    SupplierResponse getById(Long id);

    SupplierResponse create(SupplierRequest request);

    SupplierResponse update(Long id, SupplierRequest request);

    void delete(Long id);
}
