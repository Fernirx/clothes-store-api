package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.ProductVariantRequest;
import vn.fernirx.clothes.catalog.dto.response.ProductVariantResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.entity.ProductVariant;
import vn.fernirx.clothes.catalog.mapper.ProductVariantMapper;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.repository.ProductVariantRepository;
import vn.fernirx.clothes.catalog.service.ProductVariantService;
import vn.fernirx.clothes.common.exception.ResourceAlreadyExistsException;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    public List<ProductVariantResponse> getByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product");
        }
        return productVariantRepository.findByProductId(productId).stream()
                .map(productVariantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductVariantResponse getById(Long id) {
        ProductVariant variant = findVariantById(id);
        return productVariantMapper.toResponse(variant);
    }

    @Override
    @Transactional
    public ProductVariantResponse create(ProductVariantRequest request) {
        if (productVariantRepository.existsBySku(request.getSku())) {
            throw new ResourceAlreadyExistsException("ProductVariant with SKU '" + request.getSku() + "'");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product"));

        ProductVariant variant = productVariantMapper.toEntity(request);
        variant.setProduct(product);

        ProductVariant saved = productVariantRepository.save(variant);
        return productVariantMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductVariantResponse update(Long id, ProductVariantRequest request) {
        ProductVariant variant = findVariantById(id);

        if (productVariantRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new ResourceAlreadyExistsException("ProductVariant with SKU '" + request.getSku() + "'");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product"));

        variant.setProduct(product);
        productVariantMapper.updateFromRequest(request, variant);

        ProductVariant saved = productVariantRepository.save(variant);
        return productVariantMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProductVariant variant = findVariantById(id);
        productVariantRepository.delete(variant);
    }

    private ProductVariant findVariantById(Long id) {
        return productVariantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductVariant"));
    }
}
