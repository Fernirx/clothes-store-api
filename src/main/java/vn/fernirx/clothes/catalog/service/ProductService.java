package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.ProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.common.response.PageResponse;

public interface ProductService {
    PageResponse<ProductSummaryResponse> getAllByActiveTrue(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir,
            ProductFilterRequest filter);

    ProductDetailResponse getDetailBySlug(String slug);
}
