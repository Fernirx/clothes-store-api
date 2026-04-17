package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.CreateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {
    List<ProductVariantResponse> getAll(Long productId);

    ProductVariantResponse getById(Long productId, Long id);

    ProductVariantResponse create(Long productId, CreateProductVariantRequest request);

    ProductVariantResponse update(Long productId, Long id, UpdateProductVariantRequest request);

    void delete(Long productId, Long id);
}
