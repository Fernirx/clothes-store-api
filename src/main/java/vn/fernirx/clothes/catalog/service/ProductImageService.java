package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.ProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    List<ProductImageResponse> getByProductId(Long productId);

    ProductImageResponse getById(Long id);

    ProductImageResponse create(ProductImageRequest request);

    ProductImageResponse update(Long id, ProductImageRequest request);

    void delete(Long id);
}
