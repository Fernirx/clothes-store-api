package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.ProductImageRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductImageResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductImage;
import vn.fernirx.clothes.catalog.mapper.ProductImageMapper;
import vn.fernirx.clothes.catalog.repository.ProductImageRepository;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.service.ProductImageService;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    public List<ProductImageResponse> getByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product with id " + productId);
        }
        return productImageRepository.findByProductId(productId).stream()
                .map(productImageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageResponse getById(Long id) {
        ProductImage image = findImageById(id);
        return productImageMapper.toResponse(image);
    }

    @Override
    @Transactional
    public ProductImageResponse create(ProductImageRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + request.getProductId()));

        ProductImage image = productImageMapper.toEntity(request);
        image.setProduct(product);

        ProductImage saved = productImageRepository.save(image);
        return productImageMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductImageResponse update(Long id, ProductImageRequest request) {
        ProductImage image = findImageById(id);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + request.getProductId()));

        image.setProduct(product);
        image.setColor(request.getColor());
        image.setImageUrl(request.getImageUrl());

        if (request.getIsPrimary() != null) {
            image.setIsPrimary(request.getIsPrimary());
        }

        ProductImage saved = productImageRepository.save(image);
        return productImageMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProductImage image = findImageById(id);
        productImageRepository.delete(image);
    }

    private ProductImage findImageById(Long id) {
        return productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage with id " + id));
    }
}
