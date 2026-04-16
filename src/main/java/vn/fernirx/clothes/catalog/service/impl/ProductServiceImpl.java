package vn.fernirx.clothes.catalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fernirx.clothes.catalog.dto.request.AdminProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.request.ProductFilterRequest;
import vn.fernirx.clothes.catalog.dto.response.AdminProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.AdminProductSummaryResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductDetailResponse;
import vn.fernirx.clothes.catalog.dto.response.ProductSummaryResponse;
import vn.fernirx.clothes.catalog.entity.Product;
import vn.fernirx.clothes.catalog.mapper.ProductMapper;
import vn.fernirx.clothes.catalog.repository.AdminProductSpecification;
import vn.fernirx.clothes.catalog.repository.ProductRepository;
import vn.fernirx.clothes.catalog.repository.ProductSpecification;
import vn.fernirx.clothes.catalog.service.ProductService;
import vn.fernirx.clothes.common.exception.ResourceNotFoundException;
import vn.fernirx.clothes.common.response.PageResponse;
import vn.fernirx.clothes.common.util.PaginationUtil;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductSummaryResponse> getAllByActiveTrue(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir,
            ProductFilterRequest filter) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Specification<Product> specification = ProductSpecification.build(filter);
        Page<ProductSummaryResponse> data = productRepository.findAll(specification, pageable)
                .map(productMapper::toProductSummaryResponse);
        return PageResponse.of(data);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getDetailBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        return productMapper.toProductDetailResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AdminProductSummaryResponse> getProductsForAdmin(
            Integer page,
            Integer size,
            String sortBy,
            String sortDir,
            AdminProductFilterRequest filter) {
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, sortDir);
        Specification<Product> specification = AdminProductSpecification.build(filter);
        Page<AdminProductSummaryResponse> data = productRepository.findAll(specification, pageable)
                .map(productMapper::toAdminProductSummaryResponse);
        return PageResponse.of(data);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminProductDetailResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        return productMapper.toAdminProductDetailResponse(product);
    }
}
