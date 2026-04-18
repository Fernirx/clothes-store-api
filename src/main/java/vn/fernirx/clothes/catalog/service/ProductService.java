package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.AdminProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.request.CreateProductRequest;
import vn.fernirx.clothes.catalog.dto.request.ProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductRequest;
import vn.fernirx.clothes.catalog.dto.response.AdminProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.AdminProductSummaryResponse;
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

    PageResponse<AdminProductSummaryResponse> getProductsForAdmin(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir,
            AdminProductFilterRequest filter);

    AdminProductDetailResponse getById(Long id);

    AdminProductDetailResponse create(CreateProductRequest request);

    AdminProductDetailResponse update(Long id, UpdateProductRequest request);

    void delete(Long id);
}
