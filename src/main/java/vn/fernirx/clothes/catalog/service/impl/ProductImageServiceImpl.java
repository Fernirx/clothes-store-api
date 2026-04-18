package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.CreateProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductImage;
import vn.fernirx.clothes.catalog.mapper.ProductImageMapper;
import vn.fernirx.clothes.catalog.repository.ProductImageRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.ProductImageService;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    public ProductImageResponse create(Long productId, CreateProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        if (request.isPrimary()) {
            productImageRepository.resetImagePrimary(productId, request.color());
        }
        ProductImage productImage = productImageMapper.toEntity(request);
        productImage.setProduct(product);
        productImageRepository.save(productImage);
        return productImageMapper.toResponse(productImage);
    }

    @Override
    public void delete(Long productId, Long id) {
        ProductImage productImage = productImageRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        productImageRepository.delete(productImage);
    }

    @Override
    public void setImagePrimary(Long productId, Long id) {
        ProductImage productImage = productImageRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Image"));
        productImageRepository.resetImagePrimary(productId, productImage.getColor());
        productImage.setIsPrimary(true);
        productImageRepository.save(productImage);
    }
}
