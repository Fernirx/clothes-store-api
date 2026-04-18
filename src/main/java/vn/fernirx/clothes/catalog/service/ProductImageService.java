package vn.fernirx.clothes.catalog.service;

import vn.fernirx.clothes.catalog.dto.request.CreateProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;

public interface ProductImageService {
    ProductImageResponse create(Long productId, CreateProductImageRequest request);

    void delete(Long productId, Long id);

    void setImagePrimary(Long productId, Long id);
}
