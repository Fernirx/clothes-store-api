package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.ProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;

import java.util.List;

public interface ProductVariantService {

    List<ProductVariantResponse> getByProductId(Long productId);

    ProductVariantResponse getById(Long id);

    ProductVariantResponse create(ProductVariantRequest request);

    ProductVariantResponse update(Long id, ProductVariantRequest request);

    void delete(Long id);
}
