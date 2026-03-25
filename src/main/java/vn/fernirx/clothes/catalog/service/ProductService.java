package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.ProductRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductResponse;
import vn.fernirx.clothes.common.response.PageResponse;

public interface ProductService {

    PageResponse<ProductResponse> getAll(Integer page, Integer size, String sortBy, String sortDir);

    PageResponse<ProductResponse> getActive(Integer page, Integer size, String sortBy, String sortDir);

    ProductResponse getById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long id, ProductRequest request);

    void delete(Long id);
}
