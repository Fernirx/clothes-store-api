package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.CreateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.request.UpdateProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.catalog.mapper.ProductVariantMapper;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.repository.ProductVariantRepository;
import vn.fernirx.clothes.catalog.service.ProductVariantService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceInUseException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.order.repository.OrderItemRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getAll(Long productId) {
        return productVariantRepository.findAllByProductId(productId)
                .stream().map(productVariantMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponse getById(Long productId, Long id) {
        ProductVariant productVariant = productVariantRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant"));
        return productVariantMapper.toResponse(productVariant);
    }

    @Override
    public ProductVariantResponse create(Long productId, CreateProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        if (productVariantRepository
                .existsByProductIdAndSizeAndColor(productId, request.size(), request.color())) {
            throw new ResourceAlreadyExistsException("Product Variant");
        }
        if (productVariantRepository.existsBySku(request.sku())) {
            throw new ResourceAlreadyExistsException("Product Variant");
        }
        ProductVariant productVariant = productVariantMapper.toEntity(request);
        productVariant.setIsActive(true);
        productVariant.setProduct(product);
        productVariantRepository.save(productVariant);
        return productVariantMapper.toResponse(productVariant);
    }

    @Override
    public ProductVariantResponse update(Long productId, Long id, UpdateProductVariantRequest request) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product");
        }
        ProductVariant productVariant = productVariantRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant"));
        productVariantMapper.updateFromRequest(request, productVariant);
        productVariantRepository.save(productVariant);
        return productVariantMapper.toResponse(productVariant);
    }

    @Override
    public void delete(Long productId, Long id) {
        if (orderItemRepository.existsByVariant_Id(id))
            throw new ResourceInUseException("Product Variant");
        ProductVariant productVariant = productVariantRepository.findByIdAndProductId(id, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Variant"));
        productVariantRepository.delete(productVariant);
    }
}
